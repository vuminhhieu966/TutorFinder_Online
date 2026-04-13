package com.tutorfinder.model;

import java.io.Serializable;

public class ClassRoom implements Serializable {
    private static final long serialVersionUID = 1L;

    private String classId;   // Mã lớp (c1, c2...)
    private String parentId;
    private String tutorId;
    private String subject;
    private double fee;
    private int minutes;
    /**
     * Trạng thái lớp:
     * - "PENDING_FEE": Chờ gia sư đóng phí 2 buổi để kích hoạt lớp.
     * - "CLOSED": Lớp đã kích hoạt nhưng đang ở chế độ nghỉ (không vào học được).
     * - "OPEN": Gia sư đang mở cửa phòng học (Phụ huynh có thể nhấn "Vào học").
     */
    private String status;

    public ClassRoom(String classId, String parentId, String tutorId, String subject, double fee, int minutes) {
        this.classId = classId;
        this.parentId = parentId;
        this.tutorId = tutorId;
        this.subject = subject;
        this.fee = fee;
        this.minutes = minutes;
        this.status = "PENDING_FEE";
    }

    // Getters & Setters
    public String getClassId() { return classId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTutorId() { return tutorId; }
    public String getParentId() { return parentId; }
    public String getSubject() { return subject; }
    public double getFee() { return fee; }
}