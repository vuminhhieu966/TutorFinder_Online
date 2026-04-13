package com.tutorfinder.service;

import com.tutorfinder.model.*;
import java.io.*;
import java.util.*;

/**
 * Lớp DataService - Quản lý lưu trữ và xử lý dữ liệu tập trung
 */
public class DataService {
    // Các danh sách quản lý toàn bộ thực thể trong RAM
    public static List<User> allUsers = new ArrayList<>();
    public static List<Post> allPosts = new ArrayList<>();
    public static List<Request> allRequests = new ArrayList<>();
    public static List<ClassRoom> allClasses = new ArrayList<>();
    public static List<Complaint> allComplaints = new ArrayList<>();
    public static double totalRevenue = 0.0;

    // Tên file lưu trữ
    private static final String DATA_FILE = "tutor_online.dat";   // File hệ thống (Nhị phân)
    private static final String ACCOUNT_FILE = "accounts.txt";    // File xem mật khẩu (Text)

    /**
     * Hàm tự động sinh ID dựa trên tiền tố (prefix)
     * Ví dụ: prefix="t" -> trả về "t1", "t2"...
     */
    public static String generateId(String prefix) {
        int count = 1;
        // Duyệt qua danh sách tương ứng để đếm số lượng hiện có
        if (prefix.equals("t") || prefix.equals("p") || prefix.equals("admin")) {
            for (User u : allUsers) if (u.getId().startsWith(prefix)) count++;
        } else if (prefix.equals("po")) {
            count = allPosts.size() + 1;
        } else if (prefix.equals("r")) {
            count = allRequests.size() + 1;
        } else if (prefix.equals("c")) {
            count = allClasses.size() + 1;
        } else if (prefix.equals("cp")) {
            count = allComplaints.size() + 1;
        }
        return prefix + count;
    }

    /**
     * LƯU DỮ LIỆU: Ghi vào file .dat đồng thời xuất file .txt để người dùng xem
     */
    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            Map<String, Object> data = new HashMap<>();
            data.put("users", allUsers);
            data.put("posts", allPosts);
            data.put("requests", allRequests);
            data.put("classes", allClasses);
            data.put("complaints", allComplaints);
            data.put("revenue", totalRevenue);

            oos.writeObject(data); // Ghi nhị phân

            // SAU KHI LƯU FILE HỆ THỐNG -> XUẤT RA FILE CHỮ ĐỂ XEM
            exportToText();

        } catch (IOException e) {
            System.out.println("❌ Lỗi ghi file: " + e.getMessage());
        }
    }

    /**
     * NẠP DỮ LIỆU: Đọc từ file .dat lên RAM khi khởi động app
     */
    @SuppressWarnings("unchecked")
    public static void loadData() {
        File f = new File(DATA_FILE);
        if (!f.exists()) {
            initDefaultData(); // Nếu chưa có file thì tạo Admin mặc định
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            Map<String, Object> data = (Map<String, Object>) ois.readObject();
            allUsers = (List<User>) data.get("users");
            allPosts = (List<Post>) data.get("posts");
            allRequests = (List<Request>) data.get("requests");
            allClasses = (List<ClassRoom>) data.get("classes");
            allComplaints = (List<Complaint>) data.get("complaints");
            totalRevenue = (Double) data.get("revenue");
        } catch (Exception e) {
            initDefaultData();
        }
    }

    /**
     * Tạo tài khoản Admin mặc định nếu file trống
     */
    private static void initDefaultData() {
        allUsers.clear();
        allUsers.add(new Admin("admin1", "admin", "123", "Quản trị viên", "0999888777"));
        saveData();
    }

    /**
     * XUẤT FILE TEXT: Ghi ra file accounts.txt định dạng dễ đọc
     */
    private static void exportToText() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNT_FILE))) {
            writer.println("===========================================================");
            writer.println("      DANH SÁCH TÀI KHOẢN ĐĂNG KÝ (DÙNG ĐỂ TEST)          ");
            writer.println("===========================================================");
            writer.printf("%-10s | %-12s | %-15s | %-10s\n", "ID", "VAI TRÒ", "USERNAME", "PASSWORD");
            writer.println("-----------------------------------------------------------");
            for (User u : allUsers) {
                writer.printf("%-10s | %-12s | %-15s | %-10s\n",
                        u.getId(), u.getRole(), u.getUsername(), u.getPassword());
            }
            writer.println("===========================================================");
        } catch (IOException e) {
            System.out.println("⚠️ Không thể xuất file accounts.txt");
        }
    }
}