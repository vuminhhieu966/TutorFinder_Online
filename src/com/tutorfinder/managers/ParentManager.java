package com.tutorfinder.managers;

import com.tutorfinder.data.Database;
import com.tutorfinder.model.*;
import java.util.Scanner;

public class ParentManager {
    private Scanner sc;

    public ParentManager(Scanner sc) { this.sc = sc; }

    //  Tìm Gia sư
    public void findTutors() {
        System.out.println("\n--- TÌM KIẾM GIA SƯ (Nhập 0 để thoát) ---");
        System.out.print("- Môn học: "); String sub = sc.nextLine();

        if (sub.equals("0")) return; // Nút thoát

        System.out.print("- Lớp: "); String gr = sc.nextLine();
        if (gr.equals("0")) return; // Nút thoát
        System.out.print("- Khu vực: "); String ar = sc.nextLine();
        if (ar.equals("0")) return; // Nút thoát

        System.out.println("\n--- DANH SÁCH GIA SƯ ---");
        boolean found = false;
        for (User u : Database.getInstance().users) {
            if (u instanceof Tutor t && t.getStatus() == 1) {
                if (t.getSubjects().toLowerCase().contains(sub.toLowerCase()) &&
                        t.getGrades().toLowerCase().contains(gr.toLowerCase()) &&
                        t.getArea().toLowerCase().contains(ar.toLowerCase())) {

                    System.out.printf("[%s] - %s | môn dạy: %s | lớp: %s | khu vực: %s\n",
                            t.getId(), t.getName(), t.getSubjects(), t.getGrades(),t.getArea());
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("Không tìm thấy Gia sư nào.");
            return;
        }

        System.out.println("\n1.Thông tin chi tiết | 2. Đánh giá Gia sư | 0. Quay lại");
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
                    System.out.print("Đánh giá (1 đến 5sao)): ");
                    double star = Double.parseDouble(sc.nextLine());
                    t.addReview(star);
                    Database.save();
                    System.out.println("đánh giá thành công!");
                }
            }
        }
    }

    // 2. Chức năng Đăng bài
    public void createJobPost(Parent p) {
        System.out.println("\n--- ĐĂNG BÀI TÌM GIA SƯ (Nhập 0 để Hủy) ---");
        System.out.print("Môn: "); String sub = sc.nextLine();

        if (sub.equals("0")) return;

        System.out.print("Lớp: "); String gr = sc.nextLine();
        System.out.print("Khu vực: "); String ar = sc.nextLine();
        System.out.print("Giá tiền/1 buổi học (VNĐ): "); double pr = Double.parseDouble(sc.nextLine());

        String jpId = "jp" + (Database.getInstance().jIdx++); // mã bài đăng
        Database.getInstance().posts.add(new JobPost(jpId, p.getId(), sub, gr, ar, pr));
        Database.save();
        System.out.println("tạo bài đăng thành công, chờ admin duyệt.");
    }

    // 3. Chức năng Quản lý bài đăng
    public void manageMyPosts(Parent p) {
        System.out.println("\n--- CÁC BÀI ĐĂNG CỦA TÔI ---");
        boolean hasPost = false;

        for (JobPost jp : Database.getInstance().posts) {
            if (jp.getParentId().equals(p.getId())) {
                String st = jp.getStatus() == 1 ? "Đã duyệt" : "Đang chờ duyệt";
                System.out.printf("Mã bài: %s | Môn: %s | Lớp: %s | khu vực: %s | %f/buổi| Trạng thái: %s | Có %d gia sư đăng ký\n",
                        jp.getId(), jp.getSubject(), jp.getGrade(),jp.getArea(), jp.getPrice() , st, jp.getRegisteredTutorIds().size());
                hasPost = true;
            }
        }

        if (!hasPost) {
            System.out.println("Bạn chưa có bài đăng nào.");
            return;
        }

        System.out.print("\nNhập Mã bài đăng để thao tác (Hoặc Enter để Thoát): ");
        String jpId = sc.nextLine();
        if (jpId.isEmpty()) return; // Nút thoát

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
        System.out.println("\n--- BÀI ĐĂNG [" + selectedPost.getId() + "] ---");
        System.out.println("1. Xem danh sách Gia sư đăng kí nhận lớp | 2. xóa bài đăng | 0.thoát");
        System.out.print("Chọn thao tác: ");
        String choice = sc.nextLine();

        if (choice.equals("1")) {
            System.out.println(">> DANH SÁCH GIA SƯ ĐĂNG KÍ NHẬN LỚP:");
            if (selectedPost.getRegisteredTutorIds().isEmpty()) {
                System.out.println("Hiện chưa có Gia sư nào đăng ký lớp này.");
            } else {
                for (String tId : selectedPost.getRegisteredTutorIds()) {
                    for (User u : Database.getInstance().users) {
                        if (u.getId().equals(tId) && u instanceof Tutor t) {
                            System.out.printf("- Gia sư: %s | SĐT: %s | Uy tín: %.1f sao/%d lượt đánh giá\n",
                                    t.getName(), t.getPhone(), t.getRating(),t.getReviewCount());
                        }
                    }
                }
            }
        }
        else if (choice.equals("2")) {

            Database.getInstance().posts.remove(selectedPost); // Xóa khỏi danh sách RAM
            Database.save(); // Lưu đè danh sách mới xuống ổ cứng
            System.out.println("Đã xóa bài đăng!");

        }
    }
}