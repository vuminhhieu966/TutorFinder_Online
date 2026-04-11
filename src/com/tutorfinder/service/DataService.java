package com.tutorfinder.service;

import com.tutorfinder.model.*;
import java.util.ArrayList;
import java.util.List;

public class DataService {
    // Danh sách lưu trữ toàn bộ người dùng (Admin, Parent, Tutor)
    public static List<User> allUsers = new ArrayList<>();

    // Danh sách lưu trữ toàn bộ các bài đăng tìm gia sư
    public static List<Post> activePosts = new ArrayList<>();

    // Hàm khởi tạo dữ liệu mẫu để chúng ta không phải đăng ký lại mỗi khi chạy app
    public static void initData() {
        // Tạo Admin mẫu
        allUsers.add(new Admin("A01", "admin", "123", "Quản trị viên", "0912345678", "ADMIN"));

        // Tạo Phụ huynh mẫu
        allUsers.add(new Parent("P01", "phuhuynha", "123", "Nguyễn Văn A", "0988888888", "PARENT"));

        // Tạo Gia sư mẫu (Đã được duyệt sẵn)
        Tutor t1 = new Tutor("T01", "giasua", "123", "Trần Thị B", "0977777777", "TUTOR", "Toán học", "Cầu Giấy");
        t1.setApproved(true);
        t1.setBalance(500000); // Cho sẵn 500k để test
        allUsers.add(t1);

        // Tạo bài đăng mẫu
        activePosts.add(new Post("P01", "Tiếng Anh lớp 10", 1, 5, 250000, 2, "Học tối T2, T4"));
    }
}