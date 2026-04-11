package com.tutorfinder.model;

public class Post {
    private static int count = 0;
    private String postId;
    private String parentId;
    private String subject;
    private int studentCount;    // Đổi từ age sang số lượng học viên
    private int districtId;
    private double feePerLesson;
    private int sessionsPerWeek;
    private String timeNote;     // Gộp thời gian rảnh và thời gian học vào đây
    private String status;

    public Post(String parentId, String subject, int studentCount,
                int districtId, double feePerLesson, int sessionsPerWeek,
                String timeNote) {
        this.postId = String.valueOf(++count);
        this.parentId = parentId;
        this.subject = subject;
        this.studentCount = studentCount;
        this.districtId = districtId;
        this.feePerLesson = feePerLesson;
        this.sessionsPerWeek = sessionsPerWeek;
        this.timeNote = timeNote;
        this.status = "OPEN";
    }

    // Cập nhật lại hàm hiển thị cho gọn
    public void displayPost() {
        System.out.println("[" + postId + "] Môn: " + subject + " (" + studentCount + " học viên)");
        System.out.println("    Khu vực: Quận " + districtId + " - Học phí: " + feePerLesson + "đ/buổi");
        System.out.println("    Lịch trình: " + sessionsPerWeek + " buổi/tuần. Ghi chú: " + timeNote);
        System.out.println("    Trạng thái: " + status);
        System.out.println("--------------------------------------------------");
    }

    // Đừng quên các Getter nếu cần thiết ở các bước sau
    public String getPostId() { return postId; }
    public String getStatus() { return status; }
}