package com.tutorfinder.managers;

import com.tutorfinder.data.Database;
import com.tutorfinder.model.*;
import java.util.Scanner;

public class ParentManager {
    private Scanner sc;

    public ParentManager(Scanner sc) { this.sc = sc; }

    // 1. Chức năng Tìm Gia sư
    public void findTutors() {
        System.out.println("\n--- TÌM KIẾM GIA SƯ (Nhập 0 để Hủy) ---");
        System.out.print("- Môn học (VD: Toán): "); String sub = sc.nextLine();

        if (sub.equals("0")) return; // Nút thoát hiểm

        System.out.print("- Lớp (VD: Lớp 10): "); String gr = sc.nextLine();
        System.out.print("- Quận (VD: Cầu Giấy): "); String ar = sc.nextLine();

        System.out.println("\n--- DANH SÁCH GIA SƯ PHÙ HỢP ---");
        boolean found = false;
        for (User u : Database.getInstance().users) {
            if (u instanceof Tutor t && t.getStatus() == 1) {
                if (t.getSubjects().toLowerCase().contains(sub.toLowerCase()) &&
                        t.getGrades().toLowerCase().contains(gr.toLowerCase()) &&
                        t.getArea().toLowerCase().contains(ar.toLowerCase())) {

                    System.out.printf("[%s] - %s | %.1f Sao / %d lượt ĐG\n",
                            t.getId(), t.getName(), t.getRating(), t.getReviewCount());
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("Rất tiếc, không tìm thấy Gia sư nào khớp yêu cầu.");
            return;
        }

        System.out.println("\n1. Xem SĐT & Hồ sơ chi tiết | 2. Đánh giá Gia sư | 0. Quay lại");
        System.out.print("Chọn thao tác: "); String choice = sc.nextLine();

        if (choice.equals("1")) {
            System.out.print("Nhập Mã ID Gia sư (hoặc Enter để thoát): "); String id = sc.nextLine();
            if (id.isEmpty()) return;

            for (User u : Database.getInstance().users) {
                if (u.getId().equalsIgnoreCase(id) && u instanceof Tutor t) {
                    System.out.println("\n--- HỒ SƠ CHI TIẾT ---");
                    System.out.println("Họ tên: " + t.getName());
                    System.out.println("SĐT Liên hệ: " + t.getPhone());
                    System.out.println("Khu vực: " + t.getArea());
                    System.out.println("Môn dạy: " + t.getSubjects() + " | Lớp: " + t.getGrades());
                    System.out.println("Uy tín: " + t.getRating() + " Sao / " + t.getReviewCount() + " lượt");
                }
            }
        }
        else if (choice.equals("2")) {
            System.out.print("Nhập Mã ID Gia sư muốn đánh giá (Enter để thoát): "); String id = sc.nextLine();
            if (id.isEmpty()) return;

            for (User u : Database.getInstance().users) {
                if (u.getId().equalsIgnoreCase(id) && u instanceof Tutor t) {
                    System.out.print("Nhập số sao bạn muốn chấm (1 đến 5): ");
                    double star = Double.parseDouble(sc.nextLine());
                    t.addReview(star);
                    Database.save();
                    System.out.println("Cảm ơn bạn đã đánh giá!");
                }
            }
        }
    }

    // 2. Chức năng Đăng bài
    public void createJobPost(Parent p) {
        System.out.println("\n--- ĐĂNG BÀI TÌM GIA SƯ (Nhập 0 để Hủy) ---");
        System.out.print("Môn học yêu cầu: "); String sub = sc.nextLine();

        if (sub.equals("0")) return;

        System.out.print("Lớp yêu cầu: "); String gr = sc.nextLine();
        System.out.print("Khu vực / Quận: "); String ar = sc.nextLine();
        System.out.print("Giá trả trên 1 buổi (VNĐ): "); double pr = Double.parseDouble(sc.nextLine());

        String jpId = "jp" + (Database.getInstance().jIdx++);
        Database.getInstance().posts.add(new JobPost(jpId, p.getId(), sub, gr, ar, pr));
        Database.save();
        System.out.println("Đăng bài thành công! Vui lòng chờ Admin duyệt để hiển thị.");
    }

    // 3. Chức năng Quản lý bài đăng (CẬP NHẬT TÍNH NĂNG XÓA BÀI)
    public void manageMyPosts(Parent p) {
        System.out.println("\n--- CÁC BÀI ĐĂNG CỦA TÔI ---");
        boolean hasPost = false;

        for (JobPost jp : Database.getInstance().posts) {
            if (jp.getParentId().equals(p.getId())) {
                String st = jp.getStatus() == 1 ? "Đã duyệt (Đang hiện)" : "Đang chờ duyệt";
                System.out.printf("Mã bài: %s | Môn: %s | Lớp: %s | Trạng thái: %s | Có %d GS đăng ký\n",
                        jp.getId(), jp.getSubject(), jp.getGrade(), st, jp.getRegisteredTutorIds().size());
                hasPost = true;
            }
        }

        if (!hasPost) {
            System.out.println("Bạn chưa có bài đăng nào trên hệ thống.");
            return;
        }

        System.out.print("\nNhập Mã bài đăng để thao tác (Hoặc Enter để Thoát): ");
        String jpId = sc.nextLine();
        if (jpId.isEmpty()) return; // Nút thoát hiểm

        // Tìm bài đăng theo Mã ID
        JobPost selectedPost = null;
        for (JobPost jp : Database.getInstance().posts) {
            if (jp.getId().equalsIgnoreCase(jpId) && jp.getParentId().equals(p.getId())) {
                selectedPost = jp;
                break;
            }
        }

        // Báo lỗi nếu nhập sai mã
        if (selectedPost == null) {
            System.out.println("Lỗi: Không tìm thấy bài đăng này!");
            return;
        }

        // Hiện Menu thao tác cho Bài đăng vừa chọn
        System.out.println("\n--- THAO TÁC BÀI ĐĂNG [" + selectedPost.getId() + "] ---");
        System.out.println("1. Xem danh sách Gia sư muốn nhận lớp");
        System.out.println("2. Xóa bài đăng này");
        System.out.println("0. Quay lại");
        System.out.print("Chọn thao tác: ");
        String choice = sc.nextLine();

        if (choice.equals("1")) {
            System.out.println(">> DANH SÁCH GIA SƯ MUỐN NHẬN LỚP:");
            if (selectedPost.getRegisteredTutorIds().isEmpty()) {
                System.out.println("Hiện chưa có Gia sư nào đăng ký lớp này.");
            } else {
                for (String tId : selectedPost.getRegisteredTutorIds()) {
                    for (User u : Database.getInstance().users) {
                        if (u.getId().equals(tId) && u instanceof Tutor t) {
                            System.out.printf("- Gia sư: %s | SĐT: %s | Uy tín: %.1f Sao\n",
                                    t.getName(), t.getPhone(), t.getRating());
                        }
                    }
                }
            }
        }
        else if (choice.equals("2")) {
            // Xác nhận lần cuối trước khi xóa
            System.out.print("Bạn có CHẮC CHẮN muốn xóa bài đăng này không? (y/n): ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                Database.getInstance().posts.remove(selectedPost); // Xóa khỏi danh sách RAM
                Database.save(); // Lưu đè danh sách mới xuống ổ cứng
                System.out.println("Đã xóa bài đăng vĩnh viễn!");
            } else {
                System.out.println("Đã hủy thao tác xóa.");
            }
        }
    }
}