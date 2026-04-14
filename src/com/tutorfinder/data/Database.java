package com.tutorfinder.data;

import com.tutorfinder.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database implements Serializable {
    private static final long serialVersionUID = 1L;

    // Tên file lưu trữ dữ liệu gốc (Nhị phân)
    private static final String DATA_FILE = "tutor_v3.dat";
    // Tên file báo cáo để người dùng có thể mở ra xem trực tiếp (Text)
    private static final String ACCOUNTS_FILE = "accounts.txt";

    // 2 Danh sách chính chứa toàn bộ dữ liệu của hệ thống
    public List<User> users = new ArrayList<>();
    public List<JobPost> posts = new ArrayList<>();

    // Các biến đếm để tự động tạo ID (t1, p1, jp1...)
    public int tIdx = 1; // Đếm gia sư
    public int pIdx = 1; // Đếm phụ huynh
    public int jIdx = 1; // Đếm bài đăng

    // Biến lưu trữ phiên bản duy nhất của Database (Mẫu thiết kế Singleton)
    private static Database instance;

    // Hàm khởi tạo (Chỉ chạy 1 lần khi chưa có file dữ liệu)
    private Database() {
        // Tự động tạo sẵn 1 tài khoản Admin
        users.add(new Admin("admin1", "admin", "admin"));
    }

    // Hàm gọi Database ra để dùng
    public static Database getInstance() {
        if (instance == null) {
            load(); // Nếu chưa có thì đọc từ file lên
        }
        return instance;
    }


    // CƠ CHẾ ĐỒNG BỘ HAI CỬA SỔ


    // Hàm LƯU dữ liệu xuống ổ cứng
    public static void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(instance); // Ghi toàn bộ dữ liệu vào file .dat
        } catch (Exception e) {
            System.out.println("Lỗi lưu file: " + e.getMessage());
        }
        writeAccountFile(); // Tiện tay ghi luôn ra file txt để bạn đọc
    }

    // Hàm ĐỌC dữ liệu từ ổ cứng lên RAM
    public static void load() {
        File f = new File(DATA_FILE);
        if (!f.exists()) {
            // Nếu file chưa tồn tại, tạo mới và lưu lại
            if (instance == null) instance = new Database();
            save();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            Database newData = (Database) ois.readObject();
            if (instance == null) {
                instance = newData;
            } else {
                // CẬP NHẬT TRỰC TIẾP VÀO RAM: Xóa cũ, thêm mới để Cửa sổ 2 thấy dữ liệu Cửa sổ 1 ngay lập tức
                instance.users.clear(); instance.users.addAll(newData.users);
                instance.posts.clear(); instance.posts.addAll(newData.posts);
                instance.tIdx = newData.tIdx; instance.pIdx = newData.pIdx; instance.jIdx = newData.jIdx;
            }
        } catch (Exception e) {
            System.out.println("Lỗi đọc file!");
        }
    }

    // Hàm xuất dữ liệu ra file accounts.txt cho dễ nhìn
    private static void writeAccountFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            pw.println("--- DANH SÁCH TÀI KHOẢN ---");
            for (User u : instance.users) {
                pw.println("ID: " + u.getId() + " | Tài khoản: " + u.getUsername() + " | Mật khẩu: " + u.getPassword() + " | Vai trò: " + u.getRole());
            }
        } catch (Exception e) { }
    }
}