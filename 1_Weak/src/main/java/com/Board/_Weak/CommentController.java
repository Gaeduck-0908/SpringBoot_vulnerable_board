package com.Board._Weak;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class CommentController {
    private final JdbcTemplate jdbcTemplate;

    public CommentController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/comment-process")
    @ResponseBody
    public ResponseEntity<Comment> processComment(@RequestBody Map<String, String> payload) {
        String userId = payload.get("userId");
        String content = payload.get("comment");
        int postId = Integer.parseInt(payload.get("postId"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String commentDate = dateFormat.format(new Date());

        jdbcTemplate.update("INSERT INTO BOARD_COMMENTS(POST_ID, USER_ID, CONTENT, COMMENT_DATE) VALUES(?, ?, ?, TO_DATE(?, 'YYYY/MM/DD HH24:MI:SS'))",
                postId, userId, content, commentDate);

        Comment comment = jdbcTemplate.queryForObject(
                "SELECT * FROM BOARD_COMMENTS WHERE POST_ID = ? AND USER_ID = ? AND CONTENT = ? AND COMMENT_DATE = TO_DATE(?, 'YYYY/MM/DD HH24:MI:SS')",
                new Object[]{postId, userId, content, commentDate},
                new RowMapper<Comment>() {
                    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Comment comment = new Comment();
                        comment.setPostId(rs.getInt("POST_ID"));
                        comment.setUserId(rs.getString("USER_ID"));
                        comment.setContent(rs.getString("CONTENT"));
                        comment.setCommentDate(rs.getTimestamp("COMMENT_DATE"));
                        return comment;
                    }
                });

        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/comment-delete/{commentId}")
    @ResponseBody
    public ResponseEntity<String> deleteComment(@PathVariable int commentId) {
        int rowsAffected = jdbcTemplate.update("DELETE FROM BOARD_COMMENTS WHERE COMMENT_ID = ?", commentId);

        if(rowsAffected > 0) {
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } else {
            return ResponseEntity.status(400).body("댓글 삭제에 실패하였습니다.");
        }
    }
}
