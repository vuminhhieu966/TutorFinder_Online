package com.tutorfinder.service;

import com.tutorfinder.model.Post;
import java.util.Scanner;

public class ParentService {
    // Hàm đăng bài mới
    public static void createPost(Scanner sc, String parentId) {
        System.out.println("\n--- ĐĂNG BÀI TÌM GIA SƯ MỚI ---");
        System.out.print("Môn học: ");
        String subject = sc.nextLine();

        System.out.print("Số lượng học viên: ");
        int count = Integer.parseInt(sc.nextLine());

        System.out.println("Khu vực Hà Nội (1: Ba Đình, 2: Hoàn Kiếm, ..., 5: Cầu Giấy, ...)");
        System.out.print("Nhập mã quận (1-12): ");
        int districtId = Integer.parseInt(sc.nextLine());

        System.out.print("Học phí/buổi (VNĐ): ");
        double fee = Double.parseDouble(sc.nextLine());

        System.out.print("Số buổi/tuần: ");
        int sessions = Integer.parseInt(sc.nextLine());

        System.out.print("Ghi chú thời gian & thời hạn: ");
        String timeNote = sc.nextLine();

        // Tạo đối tượng bài đăng và lưu vào list chung
        Post newPost = new Post(parentId, subject, count, districtId, fee, sessions, timeNote);
        DataService.activePosts.add(newPost);

        System.out.println("✅ Đã đăng tin thành công! Mã bài đăng của bạn là: " + newPost.getPostId());
    }
}