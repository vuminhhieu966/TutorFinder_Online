package com.tutorfinder.service;

import com.tutorfinder.model.*;
import java.io.*;
import java.util.*;

public class DataService {
    public static List<User> allUsers = new ArrayList<>();
    public static List<Post> activePosts = new ArrayList<>();
    public static List<Complaint> allComplaints = new ArrayList<>();
    public static List<Enrollment> allEnrollments = new ArrayList<>();
    public static double totalRevenue = 0.0;

    private static final String FILE_NAME = "tutor_system.dat";

    // HÀM LƯU DỮ LIỆU XUỐNG FILE
    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            Map<String, Object> data = new HashMap<>();
            data.put("users", allUsers);
            data.put("posts", activePosts);
            data.put("complaints", allComplaints);
            data.put("enrollments", allEnrollments);
            data.put("revenue", totalRevenue);
            oos.writeObject(data);
        } catch (IOException e) {
            System.out.println("❌ Lỗi ghi file: " + e.getMessage());
        }
    }

    // HÀM ĐỌC DỮ LIỆU TỪ FILE (Đồng bộ Console)
    @SuppressWarnings("unchecked")
    public static void loadData() {
        File f = new File(FILE_NAME);
        if (!f.exists()) {
            initAdmin();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Map<String, Object> data = (Map<String, Object>) ois.readObject();
            allUsers = (List<User>) data.get("users");
            activePosts = (List<Post>) data.get("posts");
            allComplaints = (List<Complaint>) data.get("complaints");
            allEnrollments = (List<Enrollment>) data.get("enrollments");
            totalRevenue = (Double) data.get("revenue");
        } catch (Exception e) {
            initAdmin();
        }
    }

    private static void initAdmin() {
        allUsers.clear();
        allUsers.add(new Admin("A01", "admin", "123", "Quản trị viên", "000", "ADMIN"));
        saveData();
    }
}