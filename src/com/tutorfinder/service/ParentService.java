package com.tutorfinder.service;

import com.tutorfinder.model.Post;
import java.util.Scanner;

public class ParentService {
    public static void createPost(Scanner sc, String parentId) {
        System.out.println("\n--- ĐĂNG BÀI TÌM GIA SƯ MỚI ---");

        System.out.print("Môn học: ");
        String subject = sc.nextLine();

        System.out.print("Số lượng học viên: ");
        int count = Integer.parseInt(sc.nextLine());

        System.out.println("Chọn khu vực (1-12): 1.Ba Đình, 2.Hoàn Kiếm, 3.Tây Hồ, 4.Long Biên, 5.Cầu Giấy, 6.Đống Đa, 7.Hai Bà Trưng, 8.Hoàng Mai, 9.Thanh Xuân, 10.Nam Từ Liêm, 11.Bắc Từ Liêm, 12.Hà Đông.");
        System.out.print("Nhập số: ");
        int districtId = Integer.parseInt(sc.nextLine());

        System.out.print("Học phí mỗi buổi (VNĐ): ");
        double fee = Double.parseDouble(sc.nextLine());

        System.out.print("Số buổi/tuần: ");
        int sessions = Integer.parseInt(sc.nextLine());

        System.out.print("Ghi chú thời gian (Rảnh lúc nào, học trong bao lâu): ");
        String timeNote = sc.nextLine();

        // Tạo đối tượng Post mới với các tham số đã rút gọn
        Post newPost = new Post(parentId, subject, count, districtId, fee, sessions, timeNote);

        DataService.activePosts.add(newPost);

        System.out.println("✅ Đăng bài thành công! Mã bài: " + newPost.getPostId());
    }
}