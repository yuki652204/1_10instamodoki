package com.example.blog.form;

public class PostForm {
    private Long id;
    private String title;
    private String content;

    // Getter, Setterを必ず生成してください（右クリック -> Source -> Generate Getters and Setters）
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}