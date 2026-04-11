package com.tutorfinder.model;

public class Admin extends User {

    public Admin(String id, String username, String password, String fullName, String phoneNumber) {
        super(id, username, password, fullName, phoneNumber, "ADMIN");
    }

    @Override
    public void displayMenu() {
        System.out.println("\n--- MENU QUẢN TRỊ VIÊN ---");
        System.out.println("1. Duyệt hồ sơ gia sư mới");
        System.out.println("2. Quản lý bài đăng của phụ huynh");
        System.out.println("3. Xử lý khiếu nại & Hoàn tiền");
        System.out.println("4. Báo cáo doanh thu hệ thống");
        System.out.println("5. Khóa/Mở tài khoản người dùng");
        System.out.println("0. Đăng xuất");
    }
}