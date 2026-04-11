package com.tutorfinder.model;

public class Parent extends User {

    public Parent(String id, String username, String password, String fullName, String phoneNumber) {
        super(id, username, password, fullName, phoneNumber, "PARENT");
    }

    @Override
    public void displayMenu() {
        System.out.println("\n--- MENU PHỤ HUYNH: " + getFullName() + " ---");
        System.out.println("1. Tìm kiếm gia sư (theo Môn/Quận)");
        System.out.println("2. Đăng bài tìm gia sư mới");
        System.out.println("3. Quản lý bài đăng & Duyệt gia sư");
        System.out.println("4. Đánh giá gia sư / Khiếu nại");
        System.out.println("0. Đăng xuất");
    }
}