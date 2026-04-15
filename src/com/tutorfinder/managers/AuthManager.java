package com.tutorfinder.managers;

import java.util.Scanner;
import com.tutorfinder.data.Database;
import com.tutorfinder.model.*;

public class AuthManager {
    Scanner sc = new Scanner(System.in);

    // kiểm tra tài khoản đã rồn tại chưa
    private boolean isUsernameTaken(String username) {
        for (User u : Database.getInstance().users) {

            if (u.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    // Đăng nhập
    public User login(String username, String password) {
        Database.load();
        for (User u : Database.getInstance().users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u; // Trả về thông tin người dùng nếu đúng
            }
        }
        return null; // Trả về null nếu sai
    }

    // Đăng ký Phụ huynh (Trả về true nếu thành công, false nếu trùng tên)
    public boolean registerParent(String username, String password, String name, String phone) {
        Database.load();
        if (isUsernameTaken(username)) return false; // trùng tên

        String pId = "p" + (Database.getInstance().pIdx++);
        Database.getInstance().users.add(new Parent(pId, username, password, name, phone));
        Database.save(); // Lưu ngay xuống ổ cứng
        return true;
    }

    // Xử lý Đăng ký Gia sư
    public boolean registerTutor(String username, String password, String name, String phone,
                                 String subjects, String grades, String area) {
        Database.load();
        if (isUsernameTaken(username)) return false; // trùng tên

        String tId = "t" + (Database.getInstance().tIdx++);
        Database.getInstance().users.add(new Tutor(tId, username, password, name, phone, subjects, grades, area));
        Database.save();
        return true;
    }
    // Tính năng Đổi mật khẩu
    public void changePassword(User u) {
        Database.load();
        System.out.println("\n--- ĐỔI MẬT KHẨU ---");

        System.out.print("Mật khẩu hiện tại: ");
        String oldPass = sc.nextLine().trim(); // Thêm trim() dọn rác
        if (oldPass.equals("0") || oldPass.isEmpty()) return;

        if (!u.getPassword().equals(oldPass)) {
            System.out.println("Lỗi: Mật khẩu hiện tại không chính xác!");
            return;
        }

        System.out.print("Nhập mật khẩu MỚI: ");
        String newPass = sc.nextLine().trim(); // Thêm trim() dọn rác
        if (newPass.equals("0") || newPass.isEmpty()) return;

        System.out.print("Xác nhận lại mật khẩu MỚI: ");
        String confirmPass = sc.nextLine().trim(); // Thêm trim() dọn rác

        if (!newPass.equals(confirmPass)) {
            System.out.println("Lỗi: Mật khẩu xác nhận không khớp!");
            return;
        }

        // --- BƯỚC QUAN TRỌNG: TÌM VÀ SỬA ĐÚNG NGƯỜI TRONG DATABASE ---
        boolean isUpdated = false;
        for (User dbUser : Database.getInstance().users) {
            // Dò đúng ID của người đang đăng nhập
            if (dbUser.getId().equals(u.getId())) {
                dbUser.setPassword(newPass); // Cập nhật mật khẩu vào List gốc
                isUpdated = true;
                break;
            }
        }

        if (isUpdated) {
            // Cập nhật luôn cho cái biến u ở phiên đăng nhập hiện tại để đồng bộ
            u.setPassword(newPass);
            Database.save(); // Lưu đè danh sách mới xuống ổ cứng
            System.out.println("Đổi mật khẩu thành công!");
        } else {
            System.out.println("Lỗi hệ thống: Không tìm thấy tài khoản trong CSDL để cập nhật!");
        }
    }
}