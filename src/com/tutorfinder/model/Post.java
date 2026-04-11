package com.tutorfinder.model;

public class Post {
    private static int autoId = 0; // Biến tĩnh để tự động tăng ID
    private String postId;
    private String parentId;
    private String subject;
    private int studentCount;
    private int districtId;
    private double feePerLesson;
    private int sessionsPerWeek;
    private String timeNote;
    private String status; // OPEN (Đang tìm), CLOSED (Đã có người nhận)

    public Post(String parentId, String subject, int studentCount, int districtId, double feePerLesson, int sessionsPerWeek, String timeNote) {
        this.postId = String.valueOf(++autoId); // Tự động tạo ID 1, 2, 3...
        this.parentId = parentId;
        this.subject = subject;
        this.studentCount = studentCount;
        this.districtId = districtId;
        this.feePerLesson = feePerLesson;
        this.sessionsPerWeek = sessionsPerWeek;
        this.timeNote = timeNote;
        this.status = "OPEN";
    }

    // Hàm in thông tin bài đăng ra màn hình cho đẹp
    public void displayPost() {
        System.out.println("Mã lớp: [" + postId + "] | Môn: " + subject);
        System.out.println(" + Khu vực: Quận " + districtId + " | Học viên: " + studentCount);
        System.out.println(" + Học phí: " + feePerLesson + "đ/buổi | Lịch: " + sessionsPerWeek + " buổi/tuần");
        System.out.println(" + Ghi chú: " + timeNote);
        System.out.println("--------------------------------------------------");
    }

    // Getters
    public String getPostId() { return postId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getFeePerLesson() { return feePerLesson; }
    public int getDistrictId() { return districtId; }
}