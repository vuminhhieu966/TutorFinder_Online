package com.tutorfinder.model;

import java.io.Serializable;

public class Parent extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    public Parent(String id, String username, String password, String fullName, String phone, String role) {
        // Gọi lại constructor của lớp cha User
        super(id, username, password, fullName, phone, role);
    }

    @Override
    public void displayMenu() {
        System.out.println("\n========= MENU PHỤ HUYNH =========");
        System.out.println("1. Tìm kiếm gia sư & Gửi yêu cầu dạy");
        System.out.println("2. Đăng bài tìm gia sư");
        System.out.println("3. Quản lý bài đăng (Chọn gia sư/Xóa bài)");
        System.out.println("4. Các yêu cầu đã gửi (Xem trạng thái/Xóa)");
        System.out.println("5. Các lớp đang học (Đánh giá/Khiếu nại)");
        System.out.println("0. Đăng xuất (Hoặc nhập 'exit')");
        System.out.println("==================================");
    }
}