package com.tutorfinder.data;

import com.tutorfinder.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore implements Serializable {
    private static final long serialVersionUID = 1L;
    // Đổi tên file để làm mới hoàn toàn, tránh lỗi InvalidClassException cũ
    private static final String DATA_FILE = "tutor_system.dat";
    private static final String ACCOUNTS_FILE = "accounts.txt";

    public List<User> users = new ArrayList<>();
    public List<JobPost> jobPosts = new ArrayList<>();
    public List<Request> requests = new ArrayList<>();
    public List<Course> courses = new ArrayList<>();
    public List<Complaint> complaints = new ArrayList<>();

    public int tCount = 1, pCount = 1, jpCount = 1, rCount = 1, cCount = 1, compCount = 1;
    public double systemRevenue = 0.0;

    private static DataStore instance;

    private DataStore() {
        users.add(new Admin("a1", "admin", "admin123"));
    }

    public static DataStore getInstance() {
        if (instance == null) {
            loadData();
        }
        return instance;
    }

    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(instance);
        } catch (Exception e) { e.printStackTrace(); }
        exportAccountsToText();
    }

    public static void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            if (instance == null) instance = new DataStore();
            saveData();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            DataStore newData = (DataStore) ois.readObject();
            if (instance == null) {
                instance = newData;
            } else {
                // ĐÂY LÀ PHÉP THUẬT: Cập nhật dữ liệu mới thẳng vào RAM mà không làm thay đổi địa chỉ bộ nhớ
                // Nhờ vậy Cửa sổ 2 sẽ tự động nhận diện được dữ liệu từ Cửa sổ 1
                instance.users.clear(); instance.users.addAll(newData.users);
                instance.jobPosts.clear(); instance.jobPosts.addAll(newData.jobPosts);
                instance.requests.clear(); instance.requests.addAll(newData.requests);
                instance.courses.clear(); instance.courses.addAll(newData.courses);
                instance.complaints.clear(); instance.complaints.addAll(newData.complaints);
                instance.tCount = newData.tCount; instance.pCount = newData.pCount;
                instance.jpCount = newData.jpCount; instance.rCount = newData.rCount;
                instance.cCount = newData.cCount; instance.compCount = newData.compCount;
                instance.systemRevenue = newData.systemRevenue;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void exportAccountsToText() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ACCOUNTS_FILE))) {
            pw.println("ID,Username,Password,Role,Phone");
            for (User u : instance.users) {
                pw.printf("%s,%s,%s,%s,%s\n", u.getId(), u.getUsername(), u.getPassword(), u.getRole(), u.getPhone());
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public User findUser(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) return u;
        }
        return null;
    }

    public User getUserById(String id) {
        for (User u : users) if (u.getId().equals(id)) return u;
        return null;
    }

    public String genId(String prefix) {
        switch (prefix) {
            case "t": return "t" + (tCount++);
            case "p": return "p" + (pCount++);
            case "jp": return "jp" + (jpCount++);
            case "r": return "r" + (rCount++);
            case "c": return "c" + (cCount++);
            case "comp": return "comp" + (compCount++);
            default: return prefix;
        }
    }
}