package com.tutorfinder.model;

import java.io.Serializable;

public class Admin extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    public Admin(String id, String username, String password, String fullName, String phone, String role) {
        super(id, username, password, fullName, phone, role);
    }

    @Override
    public void displayMenu() {
        System.out.println("\n========== MENU ADMIN ==========");
        System.out.println("1. Quản lý Gia sư (Duyệt/Khóa)");
        System.out.println("2. Xử lý đơn khiếu nại (Hoàn tiền/Khóa)");
        System.out.println("3. Báo cáo doanh thu hệ thống");
        System.out.println("0. Đăng xuất");
        System.out.println("================================");
    }
}