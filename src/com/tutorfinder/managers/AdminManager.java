package com.tutorfinder.managers;

import com.tutorfinder.data.Database;
import com.tutorfinder.model.*;
import java.util.Scanner;

public class AdminManager {
    private Scanner sc;

    public AdminManager(Scanner sc) { this.sc = sc; }

    // 1. Chức năng Duyệt Gia sư
    public void manageTutors() {
        System.out.println("\n--- DANH SÁCH GIA SƯ ĐANG CHỜ DUYỆT ---");
        boolean hasPending = false;
        for (User u : Database.getInstance().users) {
            if (u instanceof Tutor t && t.getStatus() == 0) {
                System.out.printf("ID: %s | Tên: %s | Dạy môn: %s | Dạy lớp: %s | Quận: %s\n",
                        t.getId(), t.getName(), t.getSubjects(), t.getGrades(), t.getArea());
                hasPending = true;
            }
        }

        if (!hasPending) {
            System.out.println("Không có Gia sư nào chờ duyệt!");
        }

        System.out.println("\nHD: Nhập ID để Duyệt: ");
        String input = sc.nextLine(); // input lúc này chính là ID luôn

        if (input.isEmpty() || input.equals("0")) return; // Nút

        for (User u : Database.getInstance().users) {
            // so sanhs input với ID của User
            if (u.getId().equalsIgnoreCase(input) && u instanceof Tutor t) {
                t.setStatus(1); // duyệt
                Database.save();
                System.out.println("Đã duyệt tài khoản cho Gia sư " + t.getName() + " thành công!");
                return;
            }
        }
        System.out.println("Lỗi: Không tìm thấy ID Gia sư này!");
    }

    // 2. Chức năng Duyệt bài đăng của Phụ huynh
    public void manageJobPosts() {
        System.out.println("\n--- DANH SÁCH BÀI ĐĂNG CHỜ DUYỆT ---");
        boolean hasPending = false;
        for (JobPost jp : Database.getInstance().posts) {
            if (jp.getStatus() == 0) {
                System.out.printf("Mã Bài: %s | Môn: %s | Lớp: %s | Khu vực: %s | Giá: %.0f\n",
                        jp.getId(), jp.getSubject(), jp.getGrade(), jp.getArea(), jp.getPrice());
                hasPending = true;
            }
        }

        if (!hasPending) {
            System.out.println("không có bài đăng nào chờ duyệt!");
        }

        System.out.print("\nNhập Mã Bài đăng để Duyệt: ");
        String jid = sc.nextLine();

        if (jid.isEmpty() || jid.equals("0")) return; // Nút thoát hiểm

        for (JobPost jp : Database.getInstance().posts) {
            if (jp.getId().equalsIgnoreCase(jid) && jp.getStatus() == 0) {
                jp.setStatus(1); // Duyệt
                Database.save();
                System.out.println("Duyệt bài thành công!");
                return;
            }
        }
        System.out.println("Lỗi: Không tìm thấy Mã Bài đăng!");
    }
    // hiện tài khoản hệ thống
    public void viewAllAccounts() {
        System.out.println("\n--- TẤT CẢ TÀI KHOẢN TRÊN HỆ THỐNG ---");
        // In tiêu đề bảng cho đẹp
        System.out.printf("%-5s | %-15s | %-20s | %-15s\n", "ID", "Username", "Họ Tên", "Vai Trò");
        System.out.println("------------------------------------------------------------");

        for (User u : Database.getInstance().users) {
            String role = "";
            if (u instanceof Admin) role = "Admin";
            else if (u instanceof Parent) role = "Phụ huynh";
            else if (u instanceof Tutor) role = "Gia sư";

            // In từng dòng dữ liệu căn lề cho thẳng cột
            System.out.printf("%-5s | %-15s | %-20s | %-15s\n",
                    u.getId(), u.getUsername(), u.getName(), role);
        }

        System.out.println("\nẤn Enter để quay lại Menu Admin.");
        sc.nextLine(); // Dừng màn hình để Admin kịp xem
    }
}