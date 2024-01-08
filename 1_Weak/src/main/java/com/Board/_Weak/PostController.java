package com.Board._Weak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostDao PostDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable int id, Model model) {
        Post post = PostDao.getPostById(id);
        List<Comment> comments = PostDao.getCommentsByPostId(id);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        return "post.html";
    }

    @DeleteMapping("/post/{id}")
    @ResponseBody
    public ResponseEntity<String> deletePost(@PathVariable int id) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM BOARD_POST WHERE ID = ?", id);

        if(rowsAffected > 0) {
            return ResponseEntity.ok("게시글이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(400).body("게시글 삭제에 실패하였습니다.");
        }
    }

    @GetMapping("/post/{id}/edit")
    public String editPost(@PathVariable int id, Model model) {
        Post post = PostDao.getPostById(id);
        model.addAttribute("post", post);
        return "edit.html";
    }

    @PostMapping("/post/{id}/edit")
    public String updatePost(@PathVariable int id, @RequestParam("title") String title,
                             @RequestParam("content") String content, @RequestParam("file") MultipartFile file,
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

        redirectAttrs.addFlashAttribute("editSuccess", true);
        redirectAttrs.addFlashAttribute("message", "게시글이 성공적으로 수정되었습니다.");

        String sql = "UPDATE BOARD_POST SET TITLE = ?, CONTENT = ? WHERE ID = ?";
        jdbcTemplate.update(sql, title, content, id);

        return "redirect:/";
    }

    @GetMapping("/post/{id}/file")
    public ResponseEntity<Resource> serveFile(@PathVariable int id) {

        String fileName = jdbcTemplate.queryForObject("SELECT FILE_NAME FROM BOARD_POST WHERE ID = ?", new Object[]{id}, String.class);

        String fileLocation = new File(System.getProperty("user.home") + "/Downloads").getAbsolutePath() + File.separator + fileName;
        Path path = Paths.get(fileLocation);
        Resource resource = null;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

}
