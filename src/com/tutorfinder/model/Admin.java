package com.tutorfinder.model;

public class Admin extends User {
    public Admin(String id, String username, String password, String fullName, String phone, String role) {
        super(id, username, password, fullName, phone, role);
    }

    @Override
    public void displayMenu() {
        System.out.println("\n---------- MENU ADMIN ----------");
        System.out.println("1. Duyệt danh sách Gia sư mới");
        System.out.println("2. Quản lý bài đăng (Xóa/Khóa)");
        System.out.println("0. Đăng xuất");
        System.out.println("--------------------------------");
    }
}