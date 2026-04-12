package com.tutorfinder.service;

import com.tutorfinder.model.*;
import java.util.*;

public class TutorService {

    // 1. TÌM LỚP & ĐĂNG KÝ (Thanh toán 2 buổi phí)
    public static void searchAndApply(Scanner sc, Tutor t) {
        DataService.loadData();
        System.out.println("\n--- TÌM LỚP HỌC (Nhấn Enter để hiện tất cả) ---");
        System.out.print("Khu vực muốn tìm: ");
        String area = sc.nextLine().trim();

        List<Post> results = new ArrayList<>();
        for (Post p : DataService.activePosts) {
            if (p.getStatus().equalsIgnoreCase("OPEN")) {
                // Lọc theo khu vực (bỏ trống = all)
                if (area.isEmpty() || t.isMatch(p.getArea(), area)) {
                    results.add(p);
                }
            }
        }

        if (results.isEmpty()) {
            System.out.println("❌ Hiện không có lớp nào phù hợp.");
            return;
        }

        System.out.println("\n--- DANH SÁCH LỚP ---");
        for (Post p : results) {
            System.out.println("[" + p.getPostId() + "] Môn: " + p.getSubject() +
                    " | Khu vực: " + p.getArea() +
                    " | Phí 2 buổi cần nộp: " + (p.getFeePerLesson() * 2) + "đ");
        }

        System.out.print("\n➤ Nhập mã lớp để đăng ký nhận (hoặc '0' để quay lại): ");
        String choice = sc.nextLine();
        if (t.isExit(choice)) return;

        for (Post p : results) {
            if (p.getPostId().equalsIgnoreCase(choice)) {
                double fee = p.getFeePerLesson() * 2;
                if (t.getBalance() >= fee) {
                    // Trừ tiền gia sư, cộng vào quỹ Admin
                    t.setBalance(t.getBalance() - fee);
                    DataService.totalRevenue += fee;

                    // Thêm vào danh sách ứng viên
                    if (!p.getApplicantIds().contains(t.getId())) {
                        p.getApplicantIds().add(t.getId());
                        System.out.println("✅ Đăng ký thành công! Đã trừ " + fee + "đ phí nhận lớp.");
                        DataService.saveData();
                    } else {
                        System.out.println("⚠️ Bạn đã ứng tuyển lớp này rồi.");
                    }
                } else {
                    System.out.println("❌ Số dư ví không đủ! Bạn cần thêm " + (fee - t.getBalance()) + "đ.");
                }
                return;
            }
        }
    }

    // 2. QUẢN LÝ VÍ (Nạp/Rút)
    public static void manageWallet(Scanner sc, Tutor t) {
        System.out.println("\n--- VÍ CÁ NHÂN ---");
        System.out.println("Số dư hiện tại: " + t.getBalance() + " VNĐ");
        System.out.println("1. Nạp tiền | 2. Rút tiền | 0. Quay lại");
        String opt = sc.nextLine();

        try {
            if (opt.equals("1")) {
                System.out.print("Nhập số tiền nạp: ");
                double amt = Double.parseDouble(sc.nextLine());
                t.setBalance(t.getBalance() + amt);
                System.out.println("✅ Đã nạp tiền.");
            } else if (opt.equals("2")) {
                System.out.print("Nhập số tiền rút: ");
                double amt = Double.parseDouble(sc.nextLine());
                if (amt <= t.getBalance()) {
                    t.setBalance(t.getBalance() - amt);
                    System.out.println("✅ Đã gửi yêu cầu rút tiền.");
                } else {
                    System.out.println("❌ Không đủ số dư.");
                }
            }
            DataService.saveData();
        } catch (Exception e) {
            System.out.println("❌ Lỗi: Vui lòng nhập số tiền hợp lệ.");
        }
    }

    // 3. THƯ MỜI TỪ PHỤ HUYNH
    public static void handleInvitations(Scanner sc, Tutor t) {
        DataService.loadData();
        if (t.getIncomingInvitations().isEmpty()) {
            System.out.println("📭 Bạn chưa có thư mời nào.");
            return;
        }

        System.out.println("\n--- THƯ MỜI DẠY ---");
        for (String parentId : t.getIncomingInvitations()) {
            System.out.println("Phụ huynh ID: " + parentId + " đang mời bạn dạy.");
        }

        System.out.print("\n➤ Nhập ID Phụ huynh để chấp nhận (hoặc '0' quay lại): ");
        String pid = sc.nextLine();
        if (t.isExit(pid)) return;

        if (t.getIncomingInvitations().contains(pid)) {
            // Chấp nhận: Tạo một enrollment mới (Cần có postId cụ thể, ở đây làm đơn giản hóa)
            System.out.println("✅ Đã nhận lời! Hãy liên hệ với Phụ huynh " + pid);
            t.getIncomingInvitations().remove(pid);
            DataService.saveData();
        }
    }

    // 4. LỚP ĐANG DẠY & KHIẾU NẠI HOÀN TIỀN
    public static void teachingManagement(Scanner sc, Tutor t) {
        DataService.loadData();
        System.out.println("\n--- LỚP ĐANG DẠY ---");
        List<Enrollment> myClasses = new ArrayList<>();
        for (Enrollment e : DataService.allEnrollments) {
            if (e.getTutorId().equals(t.getId())) {
                System.out.println("Lớp: " + e.getPostId() + " | Phụ huynh: " + e.getParentId());
                myClasses.add(e);
            }
        }

        if (myClasses.isEmpty()) return;

        System.out.print("\n➤ Nhập mã lớp muốn khiếu nại hoàn tiền (hoặc '0'): ");
        String pid = sc.nextLine();
        if (t.isExit(pid)) return;

        System.out.print("Lý do khiếu nại (Gia sư nghỉ, PH không nghe máy...): ");
        String content = sc.nextLine();
        DataService.allComplaints.add(new Complaint("C"+System.currentTimeMillis(), t.getId(), pid, content));
        DataService.saveData();
        System.out.println("✅ Đơn khiếu nại hoàn tiền đã gửi tới Admin.");
    }
}