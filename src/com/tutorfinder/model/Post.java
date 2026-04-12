package com.tutorfinder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
    private static final long serialVersionUID = 1L;
    private String postId;
    private String parentId;
    private String subject;
    private String area;
    private double feePerLesson;
    private String status; // "OPEN", "CLOSED"
    private List<String> applicantIds; // Danh sách gia sư tự ứng tuyển

    public Post(String postId, String parentId, String subject, String area, double feePerLesson) {
        this.postId = postId;
        this.parentId = parentId;
        this.subject = subject;
        this.area = area;
        this.feePerLesson = feePerLesson;
        this.status = "OPEN";
        this.applicantIds = new ArrayList<>();
    }

    // Getters & Setters
    public String getPostId() { return postId; }
    public String getParentId() { return parentId; }
    public String getSubject() { return subject; }
    public String getArea() { return area; }
    public double getFeePerLesson() { return feePerLesson; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getApplicantIds() { return applicantIds; }
}