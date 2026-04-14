package com.tutorfinder.model;

public class Tutor extends User {
    private static final long serialVersionUID = 1L;

    private String subjects; // VD: "Toán, Lý, Hóa"
    private String grades;   // VD: "Lớp 10, Lớp 11"
    private String area;     // VD: "Cầu Giấy, Đống Đa"
    private double rating;
    private int reviewCount;
    private int status;      // 0: Chờ duyệt, 1: Đã duyệt, -1: Khóa

    public Tutor(String id, String username, String password, String name, String phone,
                 String subjects, String grades, String area) {
        super(id, username, password, name, phone);
        this.subjects = subjects;
        this.grades = grades;
        this.area = area;

        // Cài đặt mặc định khi mới đăng ký
        this.rating = 5.0;
        this.reviewCount = 0;
        this.status = 0;
    }

    @Override
    public String getRole() { return "TUTOR"; }

    // Getters
    public String getSubjects() { return subjects; }
    public String getGrades() { return grades; }
    public String getArea() { return area; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public int getStatus() { return status; }

    // Setters (Dành cho chức năng Cập nhật hồ sơ)
    public void setSubjects(String subjects) { this.subjects = subjects; }
    public void setGrades(String grades) { this.grades = grades; }
    public void setArea(String area) { this.area = area; }
    public void setStatus(int status) { this.status = status; }

    // Hàm tính toán lại số sao khi có đánh giá mới
    public void addReview(double newStar) {
        double totalStars = this.rating * this.reviewCount;
        this.reviewCount++;
        this.rating = (totalStars + newStar) / this.reviewCount;
    }
}