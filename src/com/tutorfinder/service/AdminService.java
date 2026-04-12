package com.tutorfinder.service;

import com.tutorfinder.model.*;
import java.util.Scanner;

public class AdminService {

    // 1. QUẢN LÝ GIA SƯ (Duyệt/Khóa)
    public static void manageTutors(Scanner sc, Admin admin) {
        DataService.loadData();
        System.out.println("\n--- DANH SÁCH GIA SƯ TOÀN HỆ THỐNG ---");
        for (User u : DataService.allUsers) {
            if (u instanceof Tutor t) {
                System.out.println("ID: " + t.getId() + " | Tên: " + t.getFullName() +
                        " | Trạng thái: " + (t.isApproved() ? "✅ Hoạt động" : "❌ Đang khóa/Chờ"));
            }
        }

        System.out.print("\n➤ Nhập ID Gia sư để Duyệt/Khóa (hoặc '0' để quay lại): ");
        String id = sc.nextLine();
        if (admin.isExit(id)) return;

        for (User u : DataService.allUsers) {
            if (u.getId().equalsIgnoreCase(id) && u instanceof Tutor t) {
                t.setApproved(!t.isApproved()); // Đảo ngược trạng thái
                System.out.println("✅ Đã cập nhật trạng thái cho " + t.getFullName());
                DataService.saveData();
                return;
            }
        }
    }

    // 2. XỬ LÝ KHIẾU NẠI (Hoàn tiền hoặc Bác bỏ)
    public static void resolveComplaints(Scanner sc, Admin admin) {
        DataService.loadData();
        System.out.println("\n--- CÁC ĐƠN KHIẾU NẠI ĐANG CHỜ ---");
        boolean hasPending = false;
        for (Complaint cp : DataService.allComplaints) {
            if (cp.getStatus().equalsIgnoreCase("PENDING")) {
                System.out.println("[" + cp.getId() + "] Người gửi: " + cp.getSenderId() +
                        " | Lớp: " + cp.getPostId() + " | Nội dung: " + cp.getContent());
                hasPending = true;
            }
        }

        if (!hasPending) {
            System.out.println("📭 Không có khiếu nại nào.");
            return;
        }

        System.out.print("\n➤ Nhập mã đơn (ID) để xử lý: ");
        String cpId = sc.nextLine();
        System.out.println("Hành động: 1. Hoàn tiền & Đóng đơn | 2. Bác bỏ | 0. Quay lại");
        String opt = sc.nextLine();

        if (opt.equals("1")) {
            handleRefund(cpId);
        } else if (opt.equals("2")) {
            updateComplaintStatus(cpId, "REJECTED");
            System.out.println("✅ Đã bác bỏ đơn khiếu nại.");
        }
    }

    // Logic hoàn tiền (Lấy từ Revenue trả về ví Gia sư)
    private static void handleRefund(String complaintId) {
        for (Complaint cp : DataService.allComplaints) {
            if (cp.getId().equalsIgnoreCase(complaintId)) {
                // Giả sử hoàn phí 2 buổi dựa trên bài đăng
                for (Post p : DataService.activePosts) {
                    if (p.getPostId().equalsIgnoreCase(cp.getPostId())) {
                        double refundAmt = p.getFeePerLesson() * 2;

                        // Tìm gia sư (người gửi khiếu nại) để trả tiền
                        for (User u : DataService.allUsers) {
                            if (u.getId().equalsIgnoreCase(cp.getSenderId()) && u instanceof Tutor t) {
                                t.setBalance(t.getBalance() + refundAmt);
                                DataService.totalRevenue -= refundAmt;
                                cp.setStatus("RESOLVED - REFUNDED");
                                System.out.println("✅ Đã hoàn " + refundAmt + "đ cho Gia sư " + t.getFullName());
                                DataService.saveData();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void updateComplaintStatus(String id, String status) {
        for (Complaint cp : DataService.allComplaints) {
            if (cp.getId().equalsIgnoreCase(id)) {
                cp.setStatus(status);
                DataService.saveData();
                return;
            }
        }
    }

    // 3. BÁO CÁO DOANH THU
    public static void showRevenue() {
        DataService.loadData();
        System.out.println("\n========= BÁO CÁO TÀI CHÍNH =========");
        System.out.println("💰 Tổng doanh thu hệ thống: " + DataService.totalRevenue + " VNĐ");
        System.out.println("📊 Tổng số gia sư: " + DataService.allUsers.stream().filter(u -> u instanceof Tutor).count());
        System.out.println("📊 Tổng số bài đăng: " + DataService.activePosts.size());
        System.out.println("======================================");
    }
}