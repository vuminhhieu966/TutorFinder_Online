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
    // 4. TÍNH NĂNG MỚI: Sửa thông tin hồ sơ
    public void editProfile(Tutor t) {
        System.out.println("\n--- CẬP NHẬT HỒ SƠ GIA SƯ ---");
        System.out.println("LƯU Ý: Sau khi cập nhật, tài khoản sẽ chuyển về trạng thái [CHỜ DUYỆT].");
        System.out.println(" Nhấn (Enter/0) để giữ nguyên thông tin cũ.");

        System.out.print("Họ Tên hiện tại (" + t.getName() + ") -> Mới: ");
        String name = sc.nextLine();
        if (name.equals("0")) return; // Nút thoát

        System.out.print("SĐT hiện tại (" + t.getPhone() + ") -> Mới: ");
        String phone = sc.nextLine();
        if (phone.equals("0")) return;

        System.out.print("Môn dạy hiện tại (" + t.getSubjects() + ") -> Mới: ");
        String subjects = sc.nextLine();
        if (subjects.equals("0")) return;

        System.out.print("Lớp dạy hiện tại (" + t.getGrades() + ") -> Mới: ");
        String grades = sc.nextLine();
        if (grades.equals("0")) return;

        System.out.print("Quận/Khu vực hiện tại (" + t.getArea() + ") -> Mới: ");
        String area = sc.nextLine();
        if (area.equals("0")) return;

        System.out.print("\nBạn có chắc chắn muốn lưu thay đổi và gửi Admin duyệt lại? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            // Nếu người dùng có gõ chữ mới (không để trống), thì cập nhật
            if (!name.isEmpty()) t.setName(name);
            if (!phone.isEmpty()) t.setPhone(phone);
            if (!subjects.isEmpty()) t.setSubjects(subjects);
            if (!grades.isEmpty()) t.setGrades(grades);
            if (!area.isEmpty()) t.setArea(area);

            // Giáng cấp về trạng thái Chờ duyệt
            t.setStatus(0);

            Database.save(); // Lưu ngay xuống ổ cứng
            System.out.println("Cập nhật thành công! Vui lòng chờ Admin duyệt lại hồ sơ nhé.");
        } else {
            System.out.println("Đã hủy cập nhật.");
        }
    }
}