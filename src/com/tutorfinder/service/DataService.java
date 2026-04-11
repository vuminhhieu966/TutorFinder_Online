package com.tutorfinder.service;

import com.tutorfinder.model.*;
import java.util.ArrayList;
import java.util.List;

public class DataService {
    // Danh sách dùng chung cho toàn bộ chương trình (static)
    public static List<User> allUsers = new ArrayList<>();
    public static List<Post> activePosts = new ArrayList<>();

    // Hàm này dùng để nạp sẵn vài dữ liệu mẫu, đỡ phải đăng ký lại từ đầu mỗi khi chạy
    public static void initData() {
        // Tạo 1 Admin
        allUsers.add(new Admin("AD01", "admin", "123", "Quan Tri Vien", "0999888777"));

        // Tạo 1 Gia sư (Tutor)
        Tutor t1 = new Tutor("T01", "giasua", "123", "Nguyen Van A", "0912345678", "Toan", "Cau Giay");
        t1.setApproved(true); // Cho gia sư này đã được duyệt luôn để tí nữa test cho nhanh
        allUsers.add(t1);

        // Tạo 1 Phụ huynh (Parent)
        allUsers.add(new Parent("P01", "phuhuynha", "123", "Tran Thi B", "0987654321"));

        // Tạo 1 bài đăng mẫu (Post)
        activePosts.add(new Post("P01", "Toán", 1, 5, 200000, 2, "T2-T6, học 3 tháng"));
    }
}