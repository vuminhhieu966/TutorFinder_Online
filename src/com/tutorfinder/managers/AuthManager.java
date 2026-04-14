package com.tutorfinder.managers;

import com.tutorfinder.data.Database;
import com.tutorfinder.model.*;

public class AuthManager {

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
        for (User u : Database.getInstance().users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u; // Trả về thông tin người dùng nếu đúng
            }
        }
        return null; // Trả về null nếu sai
    }

    // Đăng ký Phụ huynh (Trả về true nếu thành công, false nếu trùng tên)
    public boolean registerParent(String username, String password, String name, String phone) {
        if (isUsernameTaken(username)) return false; // trùng tên

        String pId = "p" + (Database.getInstance().pIdx++);
        Database.getInstance().users.add(new Parent(pId, username, password, name, phone));
        Database.save(); // Lưu ngay xuống ổ cứng
        return true;
    }

    // Xử lý Đăng ký Gia sư
    public boolean registerTutor(String username, String password, String name, String phone,
                                 String subjects, String grades, String area) {
        if (isUsernameTaken(username)) return false; // trùng tên

        String tId = "t" + (Database.getInstance().tIdx++);
        Database.getInstance().users.add(new Tutor(tId, username, password, name, phone, subjects, grades, area));
        Database.save();
        return true;
    }
}