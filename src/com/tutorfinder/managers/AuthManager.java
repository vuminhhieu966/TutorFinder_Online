package com.tutorfinder.managers;

import com.tutorfinder.data.DataStore;
import com.tutorfinder.model.*;

public class AuthManager {
    // Xóa biến lưu bộ nhớ đệm, luôn lấy trực tiếp từ DataStore để có bản mới nhất

    public boolean registerParent(String username, String password, String phone) {
        DataStore.loadData(); // Ép hệ thống đọc file xem Cửa sổ kia có đăng ký ai chưa
        DataStore db = DataStore.getInstance();
        if (db.findUser(username, password) != null) return false;
        db.users.add(new Parent(db.genId("p"), username, password, phone));
        DataStore.saveData();
        return true;
    }

    public boolean registerTutor(String username, String password, String phone, String subject) {
        DataStore.loadData();
        DataStore db = DataStore.getInstance();
        if (db.findUser(username, password) != null) return false;
        db.users.add(new Tutor(db.genId("t"), username, password, phone, subject));
        DataStore.saveData();
        return true;
    }

    public User login(String username, String password) {
        DataStore.loadData(); // Ép hệ thống đọc file nhị phân mới nhất TRƯỚC KHI kiểm tra đăng nhập
        return DataStore.getInstance().findUser(username, password);
    }
}