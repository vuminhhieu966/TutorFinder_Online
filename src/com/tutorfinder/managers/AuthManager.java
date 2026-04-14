package com.tutorfinder.managers;

import com.tutorfinder.data.Database;
import com.tutorfinder.model.*;

public class AuthManager {

    // Hàm dùng chung nội bộ: Kiểm tra xem Tên đăng nhập đã ai dùng chưa
    private boolean isUsernameTaken(String username) {
        for (User u : Database.getInstance().users) {
            // So sánh không phân biệt chữ hoa chữ thường (vd: 'Hoang' và 'hoang' là giống nhau)
            if (u.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    // Xử lý Đăng nhập
    public User login(String username, String password) {
        for (User u : Database.getInstance().users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u; // Trả về thông tin người dùng nếu đúng
            }
        }
        return null; // Trả về null nếu sai
    }

    // Xử lý Đăng ký Phụ huynh (Trả về true nếu thành công, false nếu trùng tên)
    public boolean registerParent(String username, String password, String name, String phone) {
        if (isUsernameTaken(username)) return false; // Chặn ngay nếu trùng

        String pId = "p" + (Database.getInstance().pIdx++);
        Database.getInstance().users.add(new Parent(pId, username, password, name, phone));
        Database.save(); // Lưu ngay xuống ổ cứng
        return true;
    }

    // Xử lý Đăng ký Gia sư
    public boolean registerTutor(String username, String password, String name, String phone,
                                 String subjects, String grades, String area) {
        if (isUsernameTaken(username)) return false; // Chặn ngay nếu trùng

        String tId = "t" + (Database.getInstance().tIdx++);
        Database.getInstance().users.add(new Tutor(tId, username, password, name, phone, subjects, grades, area));
        Database.save();
        return true;
    }
}