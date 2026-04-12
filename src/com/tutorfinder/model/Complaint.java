package com.tutorfinder.model;
import java.io.Serializable;

public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String senderId;
    private String postId;
    private String content;
    private String status; // "PENDING", "RESOLVED"

    public Complaint(String id, String senderId, String postId, String content) {
        this.id = id;
        this.senderId = senderId;
        this.postId = postId;
        this.content = content;
        this.status = "PENDING";
    }
    public String getId() { return id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSenderId() { return senderId; }
    public String getPostId() { return postId; }
    public String getContent() { return content;}

}