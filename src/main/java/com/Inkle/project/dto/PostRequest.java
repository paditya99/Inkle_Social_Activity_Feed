package com.Inkle.project.dto;

public class PostRequest {
    private String content;

    // Default constructor
    public PostRequest() {
    }

    // Constructor with fields
    public PostRequest(String content) {
        this.content = content;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
} 