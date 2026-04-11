package com.tutorfinder.model;

public class Tutor extends User {
    private String subject;     // Môn học sở trường
    private String area;        // Khu vực dạy (Quận)
    private double balance;     // Số dư ví tiền
    private boolean isApproved; // Đã được admin duyệt chưa?

    public Tutor(String id, String username, String password, String fullName, String phone, String role, String subject, String area) {
        super(id, username, password, fullName, phone, role);
        this.subject = subject;
        this.area = area;
        this.balance = 0.0;     // Mặc định đăng ký xong có 0đ
        this.isApproved = false; // Mặc định chờ duyệt
    }

    @Override
    public void displayMenu() {
        System.out.println("\n---------- MENU GIA SƯ ----------");
        System.out.println("1. Tìm kiếm lớp học (Tất cả/Theo quận)");
        System.out.println("2. Nạp tiền vào ví");
        System.out.println("3. Xem hồ sơ & Số dư ví");
        System.out.println("4. Nhận lớp (Apply)");
        System.out.println("0. Đăng xuất");
        System.out.println("---------------------------------");
    }

    // Các Getter và Setter riêng cho Gia sư
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean approved) { isApproved = approved; }
    public String getSubject() { return subject; }
    public String getArea() { return area; }
}