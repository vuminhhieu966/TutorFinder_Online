package com.tutorfinder.model;

import java.io.Serializable;

public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id, parentId, tutorId, courseId, reason;
    private double amountToRefund;
    private int status; // 0: Chờ xử lý, 1: Đã hoàn tiền, -1: Bác bỏ

    public Complaint(String id, String parentId, String tutorId, String courseId, String reason, double amountToRefund) {
        this.id = id; this.parentId = parentId; this.tutorId = tutorId;
        this.courseId = courseId; this.reason = reason; this.amountToRefund = amountToRefund;
        this.status = 0;
    }

    public String getId() { return id; }
    public String getParentId() { return parentId; }
    public String getTutorId() { return tutorId; }
    public String getCourseId() { return courseId; }
    public String getReason() { return reason; }
    public double getAmountToRefund() { return amountToRefund; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}