package com.tutorfinder.model;

import java.io.Serializable;

public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;

    private String complaintId; // Mã đơn (cp1, cp2...)
    private String senderId;    // ID người khiếu nại
    private String classId;     // Khiếu nại lớp nào
    private String content;     // Nội dung khiếu nại
    private String status;      // "PENDING" (Chờ xử lý), "RESOLVED" (Đã xong), "REJECTED" (Bác bỏ)

    public Complaint(String complaintId, String senderId, String classId, String content) {
        this.complaintId = complaintId;
        this.senderId = senderId;
        this.classId = classId;
        this.content = content;
        this.status = "PENDING";
    }

    // Getters & Setters
    public String getComplaintId() { return complaintId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getSenderId() { return senderId; }
    public String getClassId() { return classId; }
    public String getContent() { return content; }
}