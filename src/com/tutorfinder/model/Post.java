package com.tutorfinder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
    private static final long serialVersionUID = 1L;

    private String postId;          // Mã bài đăng (ví dụ: po1, po2...)
    private String parentId;        // ID người đăng
    private String subject;         // Môn học
    private double fee;             // Học phí / buổi
    private int minutes;            // Thời lượng (phút) / buổi
    private List<String> applicantIds; // Danh sách ID các gia sư đã nhấn "Đăng ký"
    private String status;          // "OPEN" (Đang tìm), "CLOSED" (Đã chọn được gia sư)

    public Post(String postId, String parentId, String subject, double fee, int minutes) {
        this.postId = postId;
        this.parentId = parentId;
        this.subject = subject;
        this.fee = fee;
        this.minutes = minutes;
        this.applicantIds = new ArrayList<>();
        this.status = "OPEN";
    }

    // Getters & Setters
    public String getPostId() { return postId; }
    public String getParentId() { return parentId; }
    public String getSubject() { return subject; }
    public double getFee() { return fee; }
    public int getMinutes() { return minutes; }
    public List<String> getApplicantIds() { return applicantIds; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}