package com.Board._Weak;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "COMMENTS")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private int COMMENT_ID;

    @Column(name = "POST_ID")
    private int postId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "COMMENT_DATE")
    private Date commentDate;

    public int getCommentId() {
        return COMMENT_ID;
    }

    public void setCommentId(int COMMENT_ID) {
        this.COMMENT_ID = COMMENT_ID;
    }

    public int getPostId() { // 게시글 ID getter 추가
        return postId;
    }

    public void setPostId(int postId) { // 게시글 ID setter 추가
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }
    // getters and setters
}
