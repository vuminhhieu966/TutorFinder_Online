package com.tutorfinder.service;

import com.tutorfinder.model.*;
import java.util.Scanner;

public class AdminService {
    private static Scanner sc = new Scanner(System.in);

    // 1. QUẢN LÝ GIA SƯ (Duyệt/Khóa/Mở khóa)
    public static void manageTutors() {
        System.out.println("\n--- QUẢN LÝ TÀI KHOẢN GIA SƯ ---");
        System.out.println("Lọc theo trạng thái: 1. PENDING | 2. APPROVED | 3. LOCKED | 0. ALL");
        String filter = sc.nextLine();

        String targetStatus = switch (filter) {
            case "1" -> "PENDING";
            case "2" -> "APPROVED";
            case "3" -> "LOCKED";
            default -> "";
        };

        for (User u : DataService.allUsers) {
            if (u instanceof Tutor t) {
                if (targetStatus.isEmpty() || t.getStatus().equals(targetStatus)) {
                    System.out.printf("[%s] Tên: %-15s | Môn: %-10s | Trạng thái: %s\n",
                            t.getId(), t.getFullName(), t.getSubject(), t.getStatus());
                }
            }
        }

        System.out.print("\n➤ Nhập ID Gia sư để xử lý (hoặc 0): ");
        String tid = sc.nextLine();
        for (User u : DataService.allUsers) {
            if (u.getId().equals(tid) && u instanceof Tutor t) {
                System.out.println("Chọn hành động: 1. Duyệt | 2. Khóa | 3. Mở khóa");
                String act = sc.nextLine();
                if (act.equals("1")) t.setStatus("APPROVED");
                else if (act.equals("2")) t.setStatus("LOCKED");
                else if (act.equals("3")) t.setStatus("APPROVED");
                System.out.println("✅ Đã cập nhật trạng thái cho " + t.getFullName());
                return;
            }
        }
    }

    // 2. XỬ LÝ KHIẾU NẠI (Hoàn tiền nếu GS sai)
    public static void handleComplaints() {
        System.out.println("\n--- DANH SÁCH ĐƠN KHIẾU NẠI ---");
        for (Complaint cp : DataService.allComplaints) {
            if (cp.getStatus().equals("PENDING")) {
                System.out.printf("[%s] Người gửi: %s | Lớp: %s | Nội dung: %s\n",
                        cp.getComplaintId(), cp.getSenderId(), cp.getClassId(), cp.getContent());
            }
        }

        System.out.print("\n➤ Nhập mã đơn để xử lý: ");
        String cpid = sc.nextLine();
        for (Complaint cp : DataService.allComplaints) {
            if (cp.getComplaintId().equals(cpid)) {
                System.out.println("Quyết định: 1. Bác bỏ (GS đúng) | 2. Chấp nhận (GS sai - Hoàn tiền)");
                String decision = sc.nextLine();

                if (decision.equals("2")) {
                    refundProcess(cp);
                    cp.setStatus("RESOLVED");
                } else {
                    cp.setStatus("REJECTED");
                }
                System.out.println("✅ Đã xử lý khiếu nại.");
                return;
            }
        }
    }

    private static void refundProcess(Complaint cp) {
        // Tìm lớp học và phụ huynh để hoàn lại phí buổi học cuối
        for (ClassRoom c : DataService.allClasses) {
            if (c.getClassId().equals(cp.getClassId())) {
                for (User u : DataService.allUsers) {
                    if (u.getId().equals(cp.getSenderId()) && u instanceof Parent p) {
                        p.setBalance(p.getBalance() + c.getFee());
                        System.out.println("💰 Đã hoàn " + c.getFee() + "đ vào ví Phụ huynh " + p.getFullName());
                    }
                }
            }
        }
    }

    // 3. BÁO CÁO DOANH THU
    public static void viewRevenue() {
        System.out.println("\n========= BÁO CÁO TÀI CHÍNH HỆ THỐNG =========");
        System.out.println("📈 Tổng doanh thu (Phí 2 buổi + 10% học phí):");
        System.out.printf("💰 Số tiền: %,.0f VNĐ\n", DataService.totalRevenue);

        int totalTutors = 0;
        int activeClasses = 0;
        for (User u : DataService.allUsers) if (u instanceof Tutor) totalTutors++;
        for (ClassRoom c : DataService.allClasses) if (!c.getStatus().equals("PENDING_FEE")) activeClasses++;

        System.out.println("👥 Tổng số gia sư: " + totalTutors);
        System.out.println("📖 Số lớp đã kích hoạt: " + activeClasses);
        System.out.println("==============================================");
    }
}