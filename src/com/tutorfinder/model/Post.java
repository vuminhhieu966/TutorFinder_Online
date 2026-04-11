package com.tutorfinder.model;

public class Post {
    private static int count = 0; // Biến static để đếm số bài đăng toàn hệ thống
    private String postId;
    private String parentId;
    private String subject;
    private int studentAge;
    private String address;
    private int districtId;      // Lưu từ 1-12 tương ứng 12 quận Hà Nội
    private double feePerLesson; // Học phí 1 buổi
    private int sessionsPerWeek; // Số buổi/tuần
    private String schedule;     // String tự do: "T2, T4 chiều"
    private String duration;     // "3 tháng", "1 học kỳ"...
    private String status;       // "OPEN" hoặc "CLOSED"

    public Post(String parentId, String subject, int studentAge, String address,
                int districtId, double feePerLesson, int sessionsPerWeek,
                String schedule, String duration) {
        this.postId = String.valueOf(++count); // Tự động tăng ID từ 1, 2, 3...
        this.parentId = parentId;
        this.subject = subject;
        this.studentAge = studentAge;
        this.address = address;
        this.districtId = districtId;
        this.feePerLesson = feePerLesson;
        this.sessionsPerWeek = sessionsPerWeek;
        this.schedule = schedule;
        this.duration = duration;
        this.status = "OPEN"; // Mặc định bài mới đăng là OPEN
    }

    // Getters
    public String getPostId() { return postId; }
    public String getParentId() { return parentId; }
    public String getSubject() { return subject; }
    public int getDistrictId() { return districtId; }
    public double getFeePerLesson() { return feePerLesson; }
    public String getStatus() { return status; }

    // Setters
    public void setStatus(String status) { this.status = status; }

    // Hàm hiển thị nhanh thông tin bài đăng để Gia sư xem
    public void displayPost() {
        System.out.println("[" + postId + "] Môn: " + subject + " - Học phí: " + feePerLesson + "đ/buổi");
        System.out.println("    Địa điểm: " + address + " (Quận " + districtId + ")");
        System.out.println("    Lịch học: " + sessionsPerWeek + " buổi/tuần (" + schedule + ")");
    }
}