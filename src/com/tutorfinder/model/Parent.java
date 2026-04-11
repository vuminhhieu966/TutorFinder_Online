package com.tutorfinder.model;

public class Parent extends User {
    public Parent(String id, String username, String password, String fullName, String phone, String role) {
        super(id, username, password, fullName, phone, role);
    }

    @Override
    public void displayMenu() {
        System.out.println("\n---------- MENU PHỤ HUYNH ----------");
        System.out.println("1. Xem các bài đăng của tôi");
        System.out.println("2. Đăng tin tìm gia sư mới");
        System.out.println("0. Đăng xuất");
        System.out.println("------------------------------------");
    }
}