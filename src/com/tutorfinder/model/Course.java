package com.tutorfinder.model;

import java.io.Serializable;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id, parentId, tutorId;
    private double feePerSession;
    // 0: Chờ GS nộp phí 2 buổi, 1: Đã nộp phí (Lớp đang ĐÓNG), 2: Lớp đang MỞ (Cho PH vào học)
    private int status;

    public Course(String id, String parentId, String tutorId, double feePerSession) {
        this.id = id; this.parentId = parentId; this.tutorId = tutorId;
        this.feePerSession = feePerSession; this.status = 0;
    }

    public String getId() { return id; }
    public String getParentId() { return parentId; }
    public String getTutorId() { return tutorId; }
    public double getFeePerSession() { return feePerSession; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}