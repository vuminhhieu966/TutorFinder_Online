package com.tutorfinder.service;

import com.tutorfinder.model.*;
import java.util.Scanner;

public class TutorService {
    private static Scanner sc = new Scanner(System.in);

    // 1. TÌM LỚP HỌC (Xem bài đăng của Phụ huynh)
    public static void findJob(Tutor t) {
        // Kiểm tra trạng thái duyệt
        if (t.getStatus().equals("PENDING")) {
            System.out.println("⚠️ Tài khoản của bạn đang chờ Admin duyệt. Chưa thể đăng ký nhận lớp!");
            return;
        }

        System.out.print("➤ Nhập môn muốn tìm (Bỏ qua nhấn Enter): ");
        String sub = sc.nextLine().trim();

        System.out.println("\n--- DANH SÁCH BÀI ĐĂNG TÌM GIA SƯ ---");
        boolean found = false;
        for (Post po : DataService.allPosts) {
            // Chỉ hiện bài đang OPEN và khớp môn
            if (po.getStatus().equals("OPEN") && (sub.isEmpty() || t.isMatch(po.getSubject(), sub))) {
                System.out.printf("[%s] Môn: %-10s | Phí: %-8.0f | Thời lượng: %d phút\n",
                        po.getPostId(), po.getSubject(), po.getFee(), po.getMinutes());
                found = true;
            }
        }

        if (!found) {
            System.out.println("❌ Hiện không có bài đăng nào phù hợp.");
            return;
        }

        System.out.print("\n➤ Nhập mã bài đăng để ứng tuyển (hoặc 0): ");
        String poId = sc.nextLine().trim();
        for (Post po : DataService.allPosts) {
            if (po.getPostId().equals(poId)) {
                if (!po.getApplicantIds().contains(t.getId())) {
                    po.getApplicantIds().add(t.getId());
                    System.out.println("✅ Đã gửi hồ sơ ứng tuyển thành công!");
                } else {
                    System.out.println("⚠️ Bạn đã ứng tuyển bài này rồi.");
                }
            }
        }
    }

    // 2. VÍ CÁ NHÂN (Nạp/Rút)
    public static void manageWallet(Tutor t) {
        System.out.println("\n--- VÍ GIA SƯ ---");
        System.out.println("💰 Số dư: " + t.getBalance() + " VNĐ");
        System.out.println("1. Nạp tiền | 2. Rút tiền | 0. Quay lại");
        String opt = sc.nextLine();
        if (opt.equals("1")) {
            System.out.print("Nhập số tiền nạp: ");
            t.setBalance(t.getBalance() + Double.parseDouble(sc.nextLine()));
            System.out.println("✅ Nạp tiền thành công!");
        } else if (opt.equals("2")) {
            System.out.print("Nhập số tiền rút: ");
            double amt = Double.parseDouble(sc.nextLine());
            if (amt <= t.getBalance()) {
                t.setBalance(t.getBalance() - amt);
                System.out.println("✅ Rút tiền thành công!");
            } else System.out.println("❌ Số dư không đủ!");
        }
    }

    // 3. CÁC LỚP ĐÃ ĐĂNG KÝ (Thanh toán phí 2 buổi để mở lớp)
    public static void manageAppliedClasses(Tutor t) {
        System.out.println("\n--- TRẠNG THÁI LỚP ỨNG TUYỂN ---");
        for (ClassRoom c : DataService.allClasses) {
            if (c.getTutorId().equals(t.getId()) && c.getStatus().equals("PENDING_FEE")) {
                System.out.printf("[%s] Môn: %-10s | Phí 2 buổi cần đóng: %.0f VNĐ\n",
                        c.getClassId(), c.getSubject(), c.getFee() * 2);
            }
        }
        System.out.print("➤ Nhập mã lớp để thanh toán phí và MỞ LỚP: ");
        String cid = sc.nextLine();
        for (ClassRoom c : DataService.allClasses) {
            if (c.getClassId().equals(cid) && c.getTutorId().equals(t.getId())) {
                double requiredFee = c.getFee() * 2;
                if (t.getBalance() >= requiredFee) {
                    t.setBalance(t.getBalance() - requiredFee);
                    DataService.totalRevenue += requiredFee; // Tiền phí chảy về túi Admin
                    c.setStatus("CLOSED"); // Chuyển sang trạng thái đã kích hoạt (đang nghỉ)
                    System.out.println("✅ Thanh toán phí thành công! Lớp [" + cid + "] đã sẵn sàng dạy.");
                } else {
                    System.out.println("❌ Ví không đủ tiền. Vui lòng nạp thêm " + (requiredFee - t.getBalance()) + " VNĐ.");
                }
            }
        }
    }

    // 4. THƯ MỜI TỪ PHỤ HUYNH
    public static void manageInvitations(Tutor t) {
        System.out.println("\n--- HỘP THƯ MỜI DẠY ---");
        for (Request r : DataService.allRequests) {
            if (r.getTutorId().equals(t.getId()) && r.getStatus().equals("PENDING")) {
                System.out.printf("[%s] PH: %s | Môn: %s | Lương: %.0f | Thời lượng: %dp\n",
                        r.getReqId(), r.getParentId(), r.getSubject(), r.getFee(), r.getMinutes());
            }
        }
        System.out.print("➤ Chọn mã yêu cầu để xử lý (1. Nhận | 2. Từ chối | 0. Quay lại): ");
        String rid = sc.nextLine();
        // ... Logic nhận/từ chối tương tự: Nhận thì tạo ClassRoom trạng thái PENDING_FEE
    }

    // 5. CÁC LỚP ĐANG DẠY (Mở phòng học Online)
    public static void manageTeachingClasses(Tutor t) {
        System.out.println("\n--- DANH SÁCH LỚP ĐANG DẠY ---");
        for (ClassRoom c : DataService.allClasses) {
            if (c.getTutorId().equals(t.getId()) && !c.getStatus().equals("PENDING_FEE")) {
                System.out.printf("[%s] Môn: %-10s | Trạng thái phòng: %s\n",
                        c.getClassId(), c.getSubject(), c.getStatus());
            }
        }
        System.out.print("➤ Nhập mã lớp để MỞ/ĐÓNG phòng học: ");
        String cid = sc.nextLine();
        for (ClassRoom c : DataService.allClasses) {
            if (c.getClassId().equals(cid)) {
                if (c.getStatus().equals("CLOSED")) {
                    c.setStatus("OPEN");
                    System.out.println("🚀 Đã MỞ phòng học. Phụ huynh có thể vào học ngay bây giờ!");
                } else {
                    c.setStatus("CLOSED");
                    System.out.println("🔒 Đã ĐÓNG phòng học.");
                }
            }
        }
    }

    // 6. XEM ĐÁNH GIÁ
    public static void viewReviews(Tutor t) {
        System.out.println("\n--- HỒ SƠ UY TÍN GIA SƯ ---");
        System.out.println("⭐ Điểm trung bình: " + String.format("%.1f", t.getRating()));
        System.out.println("💬 Tổng số lượt đánh giá: " + t.getReviewCount());
        // Có thể hiện thêm danh sách Complaint liên quan nếu muốn "gắt"
    }
}