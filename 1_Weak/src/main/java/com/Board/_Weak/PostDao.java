package com.Board._Weak;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PostDao {

    @Autowired
    private DB_Connect dbConnect;

    public Post getPostById(int id) {
        String sql = "SELECT * FROM BOARD_POST WHERE ID = ?";
        return dbConnect.getJdbcTemplate().queryForObject(sql, new Object[]{id}, new RowMapper<Post>() {
            @Override
            public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
                Post post = new Post(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("USER_ID"),
                        rs.getString("POST_DATE"),
                        rs.getString("UPDATE_DATE"),
                        rs.getString("CONTENT"),
                        rs.getString("FILE_NAME")
                );
                return post;
            }
        });
    }

    public List<Comment> getCommentsByPostId(int postId) {
        String sql = "SELECT USER_ID, CONTENT, COMMENT_DATE, COMMENT_ID FROM BOARD_COMMENTS WHERE POST_ID = ? ORDER BY COMMENT_DATE DESC";
        return dbConnect.getJdbcTemplate().query(sql, new Object[]{postId}, new BeanPropertyRowMapper<>(Comment.class));
    }
}
