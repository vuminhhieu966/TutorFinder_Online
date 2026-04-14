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
                System.out.printf("ID: %s | Tên: %s | Dạy môn: %s | Quận: %s\n",
                        t.getId(), t.getName(), t.getSubjects(), t.getArea());
                hasPending = true;
            }
        }

        if (!hasPending) {
            System.out.println("Hệ thống sạch sẽ, không có Gia sư nào chờ duyệt!");
        }

        System.out.println("\nHD: Nhập ID để Duyệt (VD: t1) | Nhập -ID để Khóa (VD: -t1)");
        System.out.print("Thao tác (Hoặc Enter để Quay lại): ");
        String input = sc.nextLine();

        if (input.isEmpty()) return; // Nút thoát hiểm

        boolean lock = input.startsWith("-");
        String tid = lock ? input.substring(1) : input; // Lọc bỏ dấu trừ để lấy ID thật

        for (User u : Database.getInstance().users) {
            if (u.getId().equalsIgnoreCase(tid) && u instanceof Tutor t) {
                t.setStatus(lock ? -1 : 1); // -1 là Khóa, 1 là Duyệt
                Database.save();
                System.out.println("Đã cập nhật trạng thái cho Gia sư " + t.getName() + " thành công!");
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
                System.out.printf("Mã Bài: %s | Môn: %s | Lớp: %s | Giá: %.0f\n",
                        jp.getId(), jp.getSubject(), jp.getGrade(), jp.getPrice());
                hasPending = true;
            }
        }

        if (!hasPending) {
            System.out.println("Tuyệt vời, không có bài đăng nào tồn đọng!");
        }

        System.out.print("\nNhập Mã Bài đăng để Duyệt hiển thị (Hoặc Enter để Quay lại): ");
        String jid = sc.nextLine();

        if (jid.isEmpty()) return; // Nút thoát hiểm

        for (JobPost jp : Database.getInstance().posts) {
            if (jp.getId().equalsIgnoreCase(jid) && jp.getStatus() == 0) {
                jp.setStatus(1); // 1 = Đã duyệt, hiện lên hệ thống
                Database.save();
                System.out.println("Duyệt bài thành công! Các Gia sư đã có thể tìm thấy bài này.");
                return;
            }
        }
        System.out.println("Lỗi: Không tìm thấy Mã Bài hoặc bài này đã được duyệt rồi!");
    }
}