package com.tutorfinder.service;

import com.tutorfinder.model.*;
import java.util.Scanner;

public class TutorService {
    // 1. Hiển thị tất cả bài đăng đang mở (OPEN)
    public static void showAllPosts() {
        System.out.println("\n--- DANH SÁCH LỚP ĐANG TÌM GIA SƯ ---");
        boolean hasPost = false;
        for (Post p : DataService.activePosts) {
            if (p.getStatus().equals("OPEN")) {
                p.displayPost();
                hasPost = true;
            }
        }
        if (!hasPost) System.out.println("Hiện không có lớp nào phù hợp.");
    }

    // 2. Tìm lớp theo quận
    public static void searchByDistrict(Scanner sc) {
        System.out.print("Nhập mã quận muốn tìm (1-12): ");
        int id = Integer.parseInt(sc.nextLine());
        for (Post p : DataService.activePosts) {
            if (p.getDistrictId() == id && p.getStatus().equals("OPEN")) {
                p.displayPost();
            }
        }
    }

    // 3. Nạp tiền
    public static void depositMoney(Scanner sc, Tutor tutor) {
        System.out.print("Nhập số tiền cần nạp: ");
        double amount = Double.parseDouble(sc.nextLine());
        tutor.setBalance(tutor.getBalance() + amount);
        System.out.println("✅ Nạp thành công! Số dư hiện tại: " + tutor.getBalance());
    }

    // 4. Xem hồ sơ
    public static void showTutorProfile(Tutor tutor) {
        System.out.println("\n--- THÔNG TIN CÁ NHÂN ---");
        System.out.println("Họ tên: " + tutor.getFullName() + " | Môn dạy: " + tutor.getSubject());
        System.out.println("Số dư ví: " + tutor.getBalance() + " VNĐ");
        System.out.println("Trạng thái duyệt: " + (tutor.isApproved() ? "✅ Đã duyệt" : "⏳ Chờ duyệt"));
    }

    // 5. Logic nhận lớp (Quan trọng nhất)
    public static void applyPost(Scanner sc, Tutor tutor) {
        if (!tutor.isApproved()) {
            System.out.println("❌ Tài khoản chưa được duyệt, không thể nhận lớp!");
            return;
        }

        System.out.print("Nhập mã bài đăng muốn nhận: ");
        String id = sc.nextLine();
        Post found = null;
        for (Post p : DataService.activePosts) {
            if (p.getPostId().equals(id) && p.getStatus().equals("OPEN")) {
                found = p;
                break;
            }
        }

        if (found != null) {
            double fee = found.getFeePerLesson() * 2; // Phí nhận lớp = 2 buổi
            if (tutor.getBalance() >= fee) {
                tutor.setBalance(tutor.getBalance() - fee);
                found.setStatus("CLOSED"); // Đóng bài đăng ngay lập tức
                System.out.println("✅ Nhận lớp thành công! Bạn bị trừ " + fee + " VNĐ phí nhận lớp.");
            } else {
                System.out.println("❌ Không đủ tiền trong ví! Cần thêm: " + (fee - tutor.getBalance()));
            }
        } else {
            System.out.println("❌ Không tìm thấy bài đăng hoặc bài đã đóng.");
        }
    }
}