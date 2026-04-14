package com.tutorfinder.managers;

import com.tutorfinder.data.Database;
import com.tutorfinder.model.*;
import java.util.Scanner;

public class ParentManager {
    private final Scanner sc;

    public ParentManager(Scanner sc) {
        this.sc = sc;
    }

    public void findTutors() {
        System.out.println("\n--- TÌM KIẾM GIA SƯ (Nhập 0 để thoát) ---");
        String subject = prompt("- Môn học: ");
        if (isCancel(subject)) return;

        String grade = prompt("- Lớp: ");
        if (isCancel(grade)) return;

        String area = prompt("- Khu vực: ");
        if (isCancel(area)) return;

        System.out.println("\n--- DANH SÁCH GIA SƯ ---");
        boolean found = false;
        for (User user : Database.getInstance().users) {
            if (user instanceof Tutor tutor && tutor.getStatus() == 1) {
                if (matches(tutor.getSubjects(), subject)
                        && matches(tutor.getGrades(), grade)
                        && matches(tutor.getArea(), area)) {
                    System.out.printf("[%s] - %s | môn dạy: %s | lớp: %s | khu vực: %s\n",
                            tutor.getId(), tutor.getName(), tutor.getSubjects(), tutor.getGrades(), tutor.getArea());
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("Không tìm thấy Gia sư nào.");
            return;
        }

        System.out.println("\n1.Thông tin chi tiết | 2. Đánh giá Gia sư | 0. Quay lại");
        String action = prompt("Chọn thao tác: ");

        if (action.equals("1")) {
            String tutorId = prompt("Nhập Mã ID Gia sư (hoặc Enter để thoát): ");
            if (tutorId.isEmpty()) return;

            Tutor tutor = findTutorById(tutorId);
            if (tutor != null) {
                System.out.println("\n--- HỒ SƠ CHI TIẾT ---");
                System.out.println("Họ tên: " + tutor.getName());
                System.out.println("SĐT Liên hệ: " + tutor.getPhone());
                System.out.println("Khu vực: " + tutor.getArea());
                System.out.println("Môn dạy: " + tutor.getSubjects() + " | Lớp: " + tutor.getGrades());
                System.out.println("Uy tín: " + tutor.getRating() + " Sao / " + tutor.getReviewCount() + " lượt");
            }
        } else if (action.equals("2")) {
            String tutorId = prompt("Nhập Mã ID Gia sư muốn đánh giá (Enter để thoát): ");
            if (tutorId.isEmpty()) return;

            Tutor tutor = findTutorById(tutorId);
            if (tutor != null) {
                System.out.print("Đánh giá (1 đến 5sao)): ");
                double star = Double.parseDouble(sc.nextLine());
                tutor.addReview(star);
                Database.save();
                System.out.println("đánh giá thành công!");
            }
        }
    }

    public void createJobPost(Parent parent) {
        System.out.println("\n--- ĐĂNG BÀI TÌM GIA SƯ (Nhập 0 để Hủy) ---");
        String subject = prompt("Môn: ");
        if (isCancel(subject)) return;

        String grade = prompt("Lớp: ");
        String area = prompt("Khu vực: ");
        System.out.print("Giá tiền/1 buổi học (VNĐ): ");
        double price = Double.parseDouble(sc.nextLine());

        String jpId = "jp" + (Database.getInstance().jIdx++);
        Database.getInstance().posts.add(new JobPost(jpId, parent.getId(), subject, grade, area, price));
        Database.save();
        System.out.println("tạo bài đăng thành công, chờ admin duyệt.");
    }

    public void manageMyPosts(Parent parent) {
        System.out.println("\n--- CÁC BÀI ĐĂNG CỦA TÔI ---");
        boolean hasPost = false;

        for (JobPost post : Database.getInstance().posts) {
            if (post.getParentId().equals(parent.getId())) {
                String status = post.getStatus() == 1 ? "Đã duyệt" : "Đang chờ duyệt";
                System.out.printf("Mã bài: %s | Môn: %s | Lớp: %s | khu vực: %s | %.0f/buổi | Trạng thái: %s | Có %d gia sư đăng ký\n",
                        post.getId(), post.getSubject(), post.getGrade(), post.getArea(), post.getPrice(), status,
                        post.getRegisteredTutorIds().size());
                hasPost = true;
            }
        }

        if (!hasPost) {
            System.out.println("Bạn chưa có bài đăng nào.");
            return;
        }

        String postId = prompt("\nNhập Mã bài đăng để thao tác (Hoặc Enter để Thoát): ");
        if (postId.isEmpty()) return;

        JobPost selectedPost = findPostById(parent, postId);
        if (selectedPost == null) {
            System.out.println("Lỗi: Không tìm thấy bài đăng này!");
            return;
        }

        System.out.println("\n--- BÀI ĐĂNG [" + selectedPost.getId() + "] ---");
        System.out.println("1. Xem danh sách Gia sư đăng kí nhận lớp | 2. xóa bài đăng | 0.thoát");
        String action = prompt("Chọn thao tác: ");

        if (action.equals("1")) {
            System.out.println(">> DANH SÁCH GIA SƯ ĐĂNG KÍ NHẬN LỚP:");
            if (selectedPost.getRegisteredTutorIds().isEmpty()) {
                System.out.println("Hiện chưa có Gia sư nào đăng ký lớp này.");
            } else {
                for (String tutorId : selectedPost.getRegisteredTutorIds()) {
                    Tutor tutor = findTutorById(tutorId);
                    if (tutor != null) {
                        System.out.printf("- Gia sư: %s | SĐT: %s | Uy tín: %.1f sao/%d lượt đánh giá\n",
                                tutor.getName(), tutor.getPhone(), tutor.getRating(), tutor.getReviewCount());
                    }
                }
            }
        } else if (action.equals("2")) {
            Database.getInstance().posts.remove(selectedPost);
            Database.save();
            System.out.println("Đã xóa bài đăng!");
        }
    }

    private String prompt(String message) {
        System.out.print(message);
        return sc.nextLine();
    }

    private boolean isCancel(String value) {
        return value != null && value.equals("0");
    }

    private boolean matches(String text, String filter) {
        return text != null && text.toLowerCase().contains(filter.toLowerCase());
    }

    private Tutor findTutorById(String id) {
        for (User user : Database.getInstance().users) {
            if (user instanceof Tutor tutor && tutor.getId().equalsIgnoreCase(id)) {
                return tutor;
            }
        }
        return null;
    }

    private JobPost findPostById(Parent parent, String postId) {
        for (JobPost post : Database.getInstance().posts) {
            if (post.getId().equalsIgnoreCase(postId) && post.getParentId().equals(parent.getId())) {
                return post;
            }
        }
        return null;
    }
}
