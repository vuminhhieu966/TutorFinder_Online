package com.tutorfinder.model;

/**
 * Lớp Parent - Người dùng vai trò Phụ huynh
 */
public class Parent extends User {
    private static final long serialVersionUID = 1L;

    private double balance; // Số dư ví cá nhân

    public Parent(String id, String username, String password, String fullName, String phone, String role) {
        super(id, username, password, fullName, phone, role);
        this.balance = 0.0; // Mặc định khi tạo tk là 0đ
    }

    @Override
    public void displayMenu() {
        System.out.println("\n--- MENU PHỤ HUYNH (" + getFullName() + " - ID: " + getId() + ") ---");
        System.out.println("1. Tìm gia sư (Lọc theo Môn/Sao/Lượt đánh giá)");
        System.out.println("2. Đăng bài tìm gia sư");
        System.out.println("3. Ví cá nhân (Nạp/Rút tiền)");
        System.out.println("--- QUẢN LÝ TÀI KHOẢN ---");
        System.out.println("4. Các đơn yêu cầu đã gửi (Xem trạng thái/Hủy)");
        System.out.println("5. Các bài đã đăng (Chọn gia sư ứng tuyển)");
        System.out.println("6. Các lớp học (Vào học/Đánh giá/Khiếu nại)");
        System.out.println("0. Đăng xuất");
        System.out.println("------------------------------------------");
    }

    // Getters và Setters cho ví tiền
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}