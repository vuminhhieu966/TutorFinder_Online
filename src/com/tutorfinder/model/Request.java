package com.tutorfinder.model;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id, parentId, tutorId, subject, courseId;
    private double feePerSession;
    private int minutes, status; // 0: Chờ, 1: Đã nhận, -1: Bị từ chối

    public Request(String id, String parentId, String tutorId, String subject, double fee, int minutes) {
        this.id = id; this.parentId = parentId; this.tutorId = tutorId;
        this.subject = subject; this.feePerSession = fee; this.minutes = minutes;
        this.status = 0; this.courseId = "";
    }

    public String getId() { return id; }
    public String getParentId() { return parentId; }
    public String getTutorId() { return tutorId; }
    public String getSubject() { return subject; }
    public double getFeePerSession() { return feePerSession; }
    public int getMinutes() { return minutes; }
    public int getStatus() { return status; }
    public String getCourseId() { return courseId; }

    public void setStatus(int status) { this.status = status; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
}