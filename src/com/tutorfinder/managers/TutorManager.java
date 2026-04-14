package com.tutorfinder.managers;

import com.tutorfinder.data.Database;
import com.tutorfinder.model.*;
import java.util.Scanner;

public class TutorManager {
    private Scanner sc;

    public TutorManager(Scanner sc) { this.sc = sc; }

    // 1. Tìm bài đăng để nhận lớp
    public void findJobPosts(Tutor t) {
        System.out.println("\n--- TÌM BÀI ĐĂNG (Nhập 0 thoát) ---");
        System.out.print("- Lọc theo Môn: "); String sub = sc.nextLine();

        if (sub.equals("0")) return; // Nút thoát

        System.out.print("- Lọc theo Quận: "); String ar = sc.nextLine();
        if (ar.equals("0")) return; // Nút thoát

        System.out.println("\n--- DANH SÁCH BÀI ĐĂNG ---");
        boolean found = false;
        for (JobPost jp : Database.getInstance().posts) {
            if (jp.getStatus() == 1 && jp.getSubject().toLowerCase().contains(sub.toLowerCase()) && jp.getArea().toLowerCase().contains(ar.toLowerCase())) {

                System.out.printf("Mã bài: %s | Môn: %s | Lớp: %s | Quận: %s | Giá: %.0f VNĐ\n",
                        jp.getId(), jp.getSubject(), jp.getGrade(), jp.getArea(), jp.getPrice());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Hiện chưa có bài đăng nào.");
            return;
        }

        System.out.print("\nNhập Mã bài đăng để đăng kí (Hoặc Enter để Thoát): ");
        String jid = sc.nextLine();
        if (jid.isEmpty()) return;

        for (JobPost jp : Database.getInstance().posts) {
            if (jp.getId().equalsIgnoreCase(jid)) {
                if (!jp.getRegisteredTutorIds().contains(t.getId())) {
                    jp.getRegisteredTutorIds().add(t.getId());
                    Database.save();
                    System.out.println("Đã đăng kí thành công!");
                } else {
                    System.out.println("Bạn đã đăng ký bài này rồi!");
                }
            }
        }
    }

    // 2. xem Đánh giá
    public void viewReviews(Tutor t) {
        System.out.println("\n--- THỐNG KÊ UY TÍN CỦA TÔI ---");
        System.out.printf("Chỉ số đánh giá: %.1f Sao / %d lượt đánh giá\n", t.getRating(), t.getReviewCount());
    }

    // 3. Quản lý các lớp đã đăng ký
    public void manageMyRegistrations(Tutor t) {
        System.out.println("\n--- CÁC LỚP TÔI ĐÃ ĐĂNG KÝ (Nhập 0 để Quay lại) ---");
        boolean hasRegistered = false;

        // Quét toàn bộ hệ thống xem Gia sư này đang có tên ở những bài nào
        for (JobPost jp : Database.getInstance().posts) {
            if (jp.getRegisteredTutorIds().contains(t.getId())) {
                System.out.printf("Mã bài: %s | Môn: %s | Lớp: %s | Quận: %s | Giá: %.0f VNĐ\n",
                        jp.getId(), jp.getSubject(), jp.getGrade(), jp.getArea(), jp.getPrice());
                hasRegistered = true;
            }
        }

        if (!hasRegistered) {
            System.out.println("Chưa đăng ký nhận lớp nào cả.");
            return;
        }

        System.out.print("\nNhập Mã bài đăng để HỦY ĐĂNG KÝ (Enter để Thoát): ");
        String jid = sc.nextLine();
        if (jid.isEmpty() || jid.equals("0")) return; // Nút thoát

        for (JobPost jp : Database.getInstance().posts) {
            if (jp.getId().equalsIgnoreCase(jid)) {
                // Rút tên Gia sư ra khỏi danh sách của bài đăng đó
                if (jp.getRegisteredTutorIds().contains(t.getId())) {
                    jp.getRegisteredTutorIds().remove(t.getId()); // Gỡ ID gia sư
                    Database.save(); // Lưu đè xuống ổ cứng
                    System.out.println("Đã hủy đăng ký thành công!");
                    return;
                } else {
                    System.out.println("Lỗi: Bạn chưa đăng ký bài này!");
                    return;
                }
            }
        }
        System.out.println("Lỗi: Không tìm thấy Mã bài đăng này!");
    }
}