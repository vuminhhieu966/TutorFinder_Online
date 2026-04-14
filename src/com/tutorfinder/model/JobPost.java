package com.tutorfinder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JobPost implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String parentId; // ID của Phụ huynh đăng bài
    private String subject;  // Môn học yêu cầu
    private String grade;    // Lớp yêu cầu (1-12)
    private String area;     // Khu vực / Quận
    private double price;    // Giá tiền trên 1 buổi

    private int status; // 0: Chờ Admin duyệt, 1: Đã duyệt (Được hiển thị)

    // Danh sách lưu ID của các Gia sư đã bấm đăng ký nhận lớp này
    private List<String> registeredTutorIds;

    public JobPost(String id, String parentId, String subject, String grade, String area, double price) {
        this.id = id;
        this.parentId = parentId;
        this.subject = subject;
        this.grade = grade;
        this.area = area;
        this.price = price;
        this.status = 0; // Mặc định tạo ra là chờ duyệt
        this.registeredTutorIds = new ArrayList<>();
    }

    // --- Các hàm Lấy thông tin (Getters) ---
    public String getId() { return id; }
    public String getParentId() { return parentId; }
    public String getSubject() { return subject; }
    public String getGrade() { return grade; }
    public String getArea() { return area; }
    public double getPrice() { return price; }
    public int getStatus() { return status; }
    public List<String> getRegisteredTutorIds() { return registeredTutorIds; }

    // --- Hàm cập nhật trạng thái duyệt ---
    public void setStatus(int status) { this.status = status; }
}