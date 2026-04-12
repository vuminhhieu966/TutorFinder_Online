package com.tutorfinder.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tutor extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String subject;
    private String area;
    private double balance;
    private double rating;
    private boolean isApproved;
    private List<String> incomingInvitations; // Lưu ID các bài đăng PH gửi yêu cầu

    public Tutor(String id, String username, String password, String fullName, String phone, String role, String subject, String area) {
        super(id, username, password, fullName, phone, role);
        this.subject = subject;
        this.area = area;
        this.balance = 0.0;
        this.rating = 5.0;
        this.isApproved = false;
        this.incomingInvitations = new ArrayList<>();
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=========== MENU GIA SƯ ===========");
        System.out.println("1. Tìm lớp & Đăng ký nhận lớp (Phí 2 buổi)");
        System.out.println("2. Ví cá nhân (Số dư: " + balance + "đ | Nạp/Rút)");
        System.out.println("3. Các lớp đang dạy (Khiếu nại hoàn tiền)");
        System.out.println("4. Thư mời dạy từ PH (" + incomingInvitations.size() + " mới)");
        System.out.println("5. Xem đánh giá (⭐: " + rating + ")");
        System.out.println("0. Đăng xuất (Hoặc nhập 'exit')");
        System.out.println("===================================");
    }

    // Getters & Setters
    public String getSubject() { return subject; }
    public String getArea() { return area; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean approved) { isApproved = approved; }
    public List<String> getIncomingInvitations() { return incomingInvitations; }
}