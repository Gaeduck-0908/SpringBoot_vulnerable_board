package com.Board._Weak;

public class Post {
    private int id;
    private String title;
    private String userId;
    private String postDate;
    private String updateDate;
    private String content;

    private String fileName;

    public Post(int id, String title, String userId, String postDate, String updateDate) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.postDate = postDate;
        this.updateDate = updateDate;
    }

    public Post(int id, String title, String userId, String postDate, String updateDate, String content, String fileName) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.postDate = postDate;
        this.updateDate = updateDate;
        this.content = content;
        this.fileName = fileName;
    }


    // getter, setter 메소드
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostDate() {
        return postDate;
    }
    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getContent() {  // 새로 추가된 메소드
        return content;
    }
    public void setContent(String content) {  // 새로 추가된 메소드
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
