package com.tutorfinder.model;

/**
 * Lớp Admin - Người quản trị hệ thống
 */
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin(String id, String username, String password, String fullName, String phone) {
        super(id, username, password, fullName, phone, "ADMIN");
    }

    @Override
    public void displayMenu() {
        System.out.println("\n========== MENU ADMIN ==========");
        System.out.println("1. Quản lý gia sư (Duyệt/Khóa/Mở khóa)");
        System.out.println("2. Xử lý đơn khiếu nại từ Phụ huynh");
        System.out.println("3. Báo cáo doanh thu hệ thống");
        System.out.println("0. Đăng xuất");
        System.out.println("================================");
    }
}