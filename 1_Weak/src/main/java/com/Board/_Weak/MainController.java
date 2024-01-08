package com.Board._Weak;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private DB_Connect dbConnect;

    @GetMapping("/")
    public String index(Model model, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        String sql = "SELECT * FROM BOARD_POST";
        JdbcTemplate jdbcTemplate = dbConnect.getJdbcTemplate();
        List<Post> posts = jdbcTemplate.query(sql, new PostRowMapper());

        model.addAttribute("posts", posts);
        return "index.html";
    }

    private static class PostRowMapper implements RowMapper<Post> {
        @Override
        public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String user_id = rs.getString("user_id");
            String post_date = rs.getString("post_date");
            String update_date = rs.getString("update_date");

            return new Post(id, title, user_id, post_date,update_date);
        }
    }
    @GetMapping("/register")
    public String register() {
        return "register.html";
    }

    @PostMapping("/register-process")
    public String register(@RequestParam("userId") String userId, @RequestParam("password") String password,
                           @RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("phone") String phone, RedirectAttributes redirectAttrs) {
        String sql = "INSERT INTO BOARD_USER (USER_ID, PASSWORD, NAME, EMAIL, PHONE) VALUES (?, ?, ?, ?, ?)";
        JdbcTemplate jdbcTemplate = dbConnect.getJdbcTemplate();
        jdbcTemplate.update(sql, userId, password, name, email, phone);

        redirectAttrs.addFlashAttribute("message", "회원가입이 완료되었습니다.");
        redirectAttrs.addFlashAttribute("logSuccess", true);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @PostMapping("/login-process")
    public String login(@RequestParam("userId") String userId, @RequestParam("password") String password, HttpSession session, RedirectAttributes redirectAttrs) {
        String sql = "SELECT COUNT(*) FROM BOARD_USER WHERE USER_ID = ? AND PASSWORD = ?";
        JdbcTemplate jdbcTemplate = dbConnect.getJdbcTemplate();
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, password}, Integer.class);

        if (count != null && count > 0) {
            session.setAttribute("userId", userId);
            redirectAttrs.addFlashAttribute("message", "로그인이 완료되었습니다.");
            redirectAttrs.addFlashAttribute("logSuccess", true);
            return "redirect:/";
        } else {
            session.invalidate();
            redirectAttrs.addFlashAttribute("message", "계정이 일치하지 않습니다.");
            redirectAttrs.addFlashAttribute("logSuccess", true);
            return "redirect:/login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttrs) {
        if (session.getAttribute("userId") == null) {
            redirectAttrs.addFlashAttribute("message", "로그인이 필요합니다.");
            redirectAttrs.addFlashAttribute("logSuccess", true);
            return "redirect:/login";
        } else {
            session.invalidate();
            redirectAttrs.addFlashAttribute("message", "로그아웃 되었습니다.");
            redirectAttrs.addFlashAttribute("logSuccess", true);
            return "redirect:/";
        }
    }

    @GetMapping("/write")
    public String write() {
        return "write.html";
    }

    @PostMapping("/write-process")
    public String write(@RequestParam("title") String title,
                        @RequestParam("content") String content,
                        @RequestParam("userId") String userId,
                        @RequestParam("file") MultipartFile file,
                        RedirectAttributes redirectAttrs) {

        String fileName = null;
        if (!file.isEmpty()) {
            fileName = file.getOriginalFilename();
            String fileLocation = new File(System.getProperty("user.home") + "/Downloads").getAbsolutePath() + File.separator + fileName;
            try {
                File dest = new File(fileLocation);
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 파일 이름을 DB에 저장
        String sql = "INSERT INTO BOARD_POST (ID, TITLE, CONTENT, USER_ID, FILE_NAME) VALUES (BOARD_POST_SEQ.NEXTVAL, ?, ?, ?, ?)";
        JdbcTemplate jdbcTemplate = dbConnect.getJdbcTemplate();
        jdbcTemplate.update(sql, title, content, userId, fileName);

        redirectAttrs.addFlashAttribute("message", "게시글이 작성되었습니다.");
        redirectAttrs.addFlashAttribute("logSuccess", true);

        return "redirect:/";
    }

    @GetMapping("/search")
    public ResponseEntity<List<Post>> search(@RequestParam String keyword) {
        String sql = "SELECT * FROM BOARD_POST WHERE TITLE LIKE ?";
        keyword = "%" + keyword + "%";
        Object[] params = new Object[] {keyword};

        List<Post> posts = dbConnect.getJdbcTemplate().query(sql, params, new RowMapper<Post>() {
            @Override
            public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
                Post post = new Post(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("USER_ID"),
                        rs.getString("POST_DATE"),
                        rs.getString("UPDATE_DATE")
                );
                return post;
            }
        });

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}