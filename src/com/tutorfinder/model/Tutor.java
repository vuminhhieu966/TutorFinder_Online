package com.tutorfinder.model;

/**
 * Lớp Tutor - Người dùng vai trò Gia sư
 */
public class Tutor extends User {
    private static final long serialVersionUID = 1L;

    private String subject;     // Môn học chuyên môn
    private double balance;     // Ví tiền (Dùng để đóng phí 2 buổi khi nhận lớp)
    private String status;      // Trạng thái: "PENDING" (Chờ duyệt), "APPROVED" (Đã duyệt), "LOCKED" (Bị khóa)
    private double rating;      // Điểm sao trung bình
    private int reviewCount;    // Tổng số lượt đánh giá

    public Tutor(String id, String username, String password, String fullName, String phone, String role, String subject) {
        super(id, username, password, fullName, phone, role);
        this.subject = subject;
        this.balance = 0.0;
        this.status = "PENDING"; // Đăng ký xong phải chờ Admin duyệt
        this.rating = 5.0;       // Mới đăng ký cho 5 sao làm vốn
        this.reviewCount = 0;
    }

    @Override
    public void displayMenu() {
        System.out.println("\n--- MENU GIA SƯ (" + getFullName() + " - [" + status + "]) ---");

        // Nếu bị khóa thì MainApp sẽ chặn không cho vào đây, nhưng viết ở đây cho chắc
        if (status.equals("LOCKED")) {
            System.out.println("Tài khoản bị khóa. Vui lòng liên hệ Admin.");
            return;
        }

        System.out.println("1. Tìm lớp học từ Phụ huynh");
        System.out.println("2. Ví cá nhân (Nạp/Rút/Số dư: " + balance + "đ)");
        System.out.println("--- QUẢN LÝ TÀI KHOẢN ---");
        System.out.println("3. Các lớp đã đăng ký nhận (Chờ thanh toán phí)");
        System.out.println("4. Thư mời trực tiếp từ Phụ huynh");
        System.out.println("5. Các lớp đang dạy (Mở lớp cho PH vào học)");
        System.out.println("6. Xem đánh giá cá nhân (" + rating + " ⭐ | " + reviewCount + " lượt)");
        System.out.println("0. Đăng xuất");
        System.out.println("------------------------------------------");
    }

    // Getters và Setters cho các thuộc tính đặc thù
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
}