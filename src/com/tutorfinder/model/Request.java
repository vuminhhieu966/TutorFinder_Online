package com.tutorfinder.model;

import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reqId;      // Mã yêu cầu (r1, r2...)
    private String parentId;
    private String tutorId;
    private String subject;
    private double fee;
    private int minutes;
    private String status;     // "PENDING" (Chờ), "ACCEPTED" (Nhận), "REJECTED" (Từ chối), "CANCELLED" (Hủy)

    public Request(String reqId, String parentId, String tutorId, String subject, double fee, int minutes) {
        this.reqId = reqId;
        this.parentId = parentId;
        this.tutorId = tutorId;
        this.subject = subject;
        this.fee = fee;
        this.minutes = minutes;
        this.status = "PENDING";
    }

    // Getters & Setters
    public String getReqId() { return reqId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTutorId() { return tutorId; }
    public String getParentId() { return parentId; }
    public String getSubject() { return subject; }
    public double getFee() { return fee; }
    public int getMinutes() { return minutes; }
}