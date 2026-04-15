package com.tutorfinder.managers;

import com.tutorfinder.data.Database;
import com.tutorfinder.model.*;
import java.util.Scanner;

public class ParentManager {
    private Scanner sc;

    public ParentManager(Scanner sc) { this.sc = sc; }

    // Tìm Gia sư, xem thông tin, đánh giá
    public void findTutors() {
        Database.load();
        System.out.println("\n--- TÌM KIẾM GIA SƯ ('0' để thoát) ---");
        System.out.print("- Môn học: "); String sub = sc.nextLine().trim();
        if (sub.equals("0")) return;

        System.out.print("- Lớp: "); String gr = sc.nextLine().trim();
        if (gr.equals("0")) return;

        System.out.print("- Khu vực: "); String ar = sc.nextLine().trim();
        if (ar.equals("0")) return;

        System.out.println("\n--- DANH SÁCH GIA SƯ ---");
        boolean found = false;
        for (User u : Database.getInstance().users) {
            if (u instanceof Tutor t && t.getStatus() == 1) {
                if (t.getSubjects().toLowerCase().contains(sub.toLowerCase()) &&
                        t.getGrades().toLowerCase().contains(gr.toLowerCase()) &&
                        t.getArea().toLowerCase().contains(ar.toLowerCase())) {

                    System.out.printf("[%s] - %s | Môn dạy: %s | Lớp: %s | Khu vực: %s\n",
                            t.getId(), t.getName(), t.getSubjects(), t.getGrades(), t.getArea());
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("Không tìm thấy Gia sư nào phù hợp.");
            return;
        }

        System.out.print("\nNhập Mã ID Gia sư để thao tác (Hoặc Enter để thoát): ");
        String targetId = sc.nextLine().trim();
        if (targetId.isEmpty()) return;

        // Dò tìm xem ID vừa nhập có tồn tại và là Gia sư không
        Tutor selectedTutor = null;
        for (User u : Database.getInstance().users) {
            if (u.getId().equalsIgnoreCase(targetId) && u instanceof Tutor t && t.getStatus() == 1) {
                selectedTutor = t;
                break;
            }
        }

        if (selectedTutor == null) {
            System.out.println("Lỗi: nhập sai Id!");
            return;
        }

        // --- LUỒNG MỚI: CHỌN HÀNH ĐỘNG SAU ---
        System.out.println("\n--- THAO TÁC VỚI GIA SƯ: " + selectedTutor.getName().toUpperCase() + " ---");
        System.out.println("1. Xem Thông tin chi tiết | 2. Đánh giá Gia sư | 0. Quay lại");
        System.out.print("Chọn thao tác: ");
        String choice = sc.nextLine().trim();

        if (choice.equals("1")) {
            System.out.println("\n--- HỒ SƠ CHI TIẾT ---");
            System.out.println("- Họ tên: " + selectedTutor.getName());
            System.out.println("- SĐT Liên hệ: " + selectedTutor.getPhone());
            System.out.println("- Khu vực: " + selectedTutor.getArea());
            System.out.println("- Môn dạy: " + selectedTutor.getSubjects() + " | Lớp: " + selectedTutor.getGrades());
            System.out.println("- Uy tín: " + selectedTutor.getRating() + " Sao / " + selectedTutor.getReviewCount() + " lượt đánh giá");
        }
        else if (choice.equals("2")) {
            double star = 0;
            // Vòng lặp bọc giáp chống sập App khi nhập sai định dạng
            while (true) {
                try {
                    System.out.print("Nhập điểm đánh giá (Từ 1 đến 5 sao): ");
                    star = Double.parseDouble(sc.nextLine().trim());

                    if (star >= 1 && star <= 5) {
                        break; // Nhập chuẩn thì thoát vòng lặp đi tiếp
                    } else {
                        System.out.println("Lỗi: phải trong khoảng 1-5 sao!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Lỗi:Vui lòng chỉ nhập số 1-5!");
                }
            }

            selectedTutor.addReview(star);
            Database.save(); // Lưu ngay xuống ổ cứng
            System.out.println("Đánh giá " + star + " sao cho Gia sư [" + selectedTutor.getName() + "] thành công.");
        }
    }

    // 2. Chức năng Đăng bài
    public void createJobPost(Parent p) {
        Database.load();
        System.out.println("\n--- ĐĂNG BÀI TÌM GIA SƯ ---");

        System.out.print("Môn: ");
        String sub = sc.nextLine();
        if (sub.equals("0") || sub.isEmpty()) return;   // nút thoát

        System.out.print("Lớp: ");
        String gr = sc.nextLine();
        if (gr.equals("0") || gr.isEmpty()) return ;

        System.out.print("Khu vực: ");
        String ar = sc.nextLine();
        if (ar.equals("0") || ar.isEmpty()) return;

        double pr = 0;
        while (true) {
            try {
                System.out.print("Giá tiền/1 buổi học (VNĐ): ");
                String inputPrice = sc.nextLine();

                if (inputPrice.equals("0") || inputPrice.isEmpty()) return;

                // ép kiểu từ Chữ sang Số thập phân
                pr = Double.parseDouble(inputPrice);

                if (pr < 0) {
                    System.out.println("Lỗi: Giá tiền không được là số âm. Vui lòng nhập lại!");
                    continue;
                }

                break; // Nếu code chạy được đến đây nghĩa là nhập đúng số -> phá vòng lặp đi tiếp

            } catch (NumberFormatException e) {
                // Nếu ép kiểu thất bại (người dùng gõ chữ), nó sẽ rớt xuống đây thay vì sập App
                System.out.println("Lỗi: Vui lòng CHỈ NHẬP SỐ!");
            }
        }

        // Tạo bài đăng và lưu Database
        String jpId = "jp" + (Database.getInstance().jIdx++); // mã bài đăng
        Database.getInstance().posts.add(new JobPost(jpId, p.getId(), sub, gr, ar, pr));
        Database.save(); // Lưu ngay xuống file
        System.out.println("tạo bài đăng thành công! Vui lòng chờ Admin duyệt.");
    }

    // 3. Chức năng Quản lý bài đăng
    public void manageMyPosts(Parent p) {
        Database.load();
        System.out.println("\n--- CÁC BÀI ĐĂNG CỦA TÔI ---");
        boolean hasPost = false;

        for (JobPost jp : Database.getInstance().posts) {
            if (jp.getParentId().equals(p.getId())) {
                String st = jp.getStatus() == 1 ? "Đã duyệt" : "Đang chờ duyệt";

                System.out.printf("Mã bài: %s | Môn: %s | Lớp: %s | Khu vực: %s | %,.0f VNĐ/buổi | Trạng thái: %s | Có %d gia sư đăng ký\n",
                        jp.getId(), jp.getSubject(), jp.getGrade(), jp.getArea(), jp.getPrice(), st, jp.getRegisteredTutorIds().size());
                hasPost = true;
            }
        }

        if (!hasPost) {
            System.out.println("Bạn chưa có bài đăng nào.");
            return;
        }

        System.out.print("\nNhập Mã bài đăng để thao tác (Hoặc Enter để Thoát): ");
        String jpId = sc.nextLine().trim(); // Thêm trim() để lỡ gõ thừa dấu cách vẫn nhận diện được mã
        if (jpId.isEmpty() || jpId.equals("0")) return; // Nút thoát

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
        System.out.println("1. Xem danh sách Gia sư đăng kí nhận lớp | 2. Xóa bài đăng | 0. Thoát");
        System.out.print("Chọn thao tác: ");
        String choice = sc.nextLine().trim();

        if (choice.equals("1")) {
            System.out.println(">> DANH SÁCH GIA SƯ ĐĂNG KÍ NHẬN LỚP:");
            if (selectedPost.getRegisteredTutorIds().isEmpty()) {
                System.out.println("Hiện chưa có Gia sư nào đăng ký lớp này.");
            } else {
                boolean foundTutor = false;
                for (String tId : selectedPost.getRegisteredTutorIds()) {
                    for (User u : Database.getInstance().users) {
                        if (u.getId().trim().equalsIgnoreCase(tId.trim()) && u instanceof Tutor t) {
                            System.out.printf("- Gia sư: %s | SĐT: %s | Uy tín: %.1f sao/%d lượt đánh giá\n",
                                    t.getName(), t.getPhone(), t.getRating(), t.getReviewCount());
                            foundTutor = true;
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