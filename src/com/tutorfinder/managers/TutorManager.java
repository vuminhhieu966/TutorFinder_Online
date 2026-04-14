package com.tutorfinder.managers;

import com.tutorfinder.data.Database;
import com.tutorfinder.model.*;
import java.util.Scanner;

public class TutorManager {
    private Scanner sc;

    public TutorManager(Scanner sc) { this.sc = sc; }

    // 1. Chức năng Tìm bài đăng để nhận lớp
    public void findJobPosts(Tutor t) {
        System.out.println("\n--- TÌM LỚP ĐỂ DẠY (Nhập 0 để Hủy) ---");
        System.out.println("Nhập từ khóa lọc (Để trống và ấn Enter nếu muốn xem tất cả)");
        System.out.print("- Lọc theo Môn: "); String sub = sc.nextLine();

        if (sub.equals("0")) return; // Nút thoát hiểm

        System.out.print("- Lọc theo Quận: "); String ar = sc.nextLine();

        System.out.println("\n--- DANH SÁCH BÀI ĐĂNG PHÙ HỢP ---");
        boolean found = false;
        for (JobPost jp : Database.getInstance().posts) {
            // Chỉ hiện bài đã được Admin duyệt
            if (jp.getStatus() == 1 &&
                    jp.getSubject().toLowerCase().contains(sub.toLowerCase()) &&
                    jp.getArea().toLowerCase().contains(ar.toLowerCase())) {

                System.out.printf("Mã bài: %s | Môn: %s | Lớp: %s | Quận: %s | Giá: %.0f VNĐ\n",
                        jp.getId(), jp.getSubject(), jp.getGrade(), jp.getArea(), jp.getPrice());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Hiện chưa có lớp nào phù hợp với bạn.");
            return;
        }

        System.out.print("\nNhập Mã bài đăng bạn muốn dạy (Hoặc Enter để Thoát): ");
        String jid = sc.nextLine();
        if (jid.isEmpty()) return; // Nút thoát hiểm

        for (JobPost jp : Database.getInstance().posts) {
            if (jp.getId().equalsIgnoreCase(jid)) {
                // Kiểm tra xem Gia sư này đã ấn đăng ký bài này trước đó chưa
                if (!jp.getRegisteredTutorIds().contains(t.getId())) {
                    jp.getRegisteredTutorIds().add(t.getId()); // Ghi danh vào danh sách
                    Database.save();
                    System.out.println("Đã ghi danh thành công! Phụ huynh sẽ xem số điện thoại và liên hệ cho bạn.");
                } else {
                    System.out.println("Bạn đã đăng ký nhận lớp này rồi!");
                }
            }
        }
    }

    // 2. Chức năng xem Đánh giá
    public void viewReviews(Tutor t) {
        System.out.println("\n--- THỐNG KÊ UY TÍN CỦA TÔI ---");
        System.out.printf("Chỉ số đánh giá: %.1f Sao / %d lượt đánh giá\n", t.getRating(), t.getReviewCount());
        if (t.getReviewCount() == 0) {
            System.out.println("(Bạn là Gia sư mới, hãy nhận lớp để có thêm lượt đánh giá nhé!)");
        }
    }
}