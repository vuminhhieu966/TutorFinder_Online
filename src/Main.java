package com.tutorfinder;

import com.tutorfinder.model.*;
import com.tutorfinder.service.*;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        DataService.loadData();
        while (true) {
            DataService.loadData();
            System.out.println("\n--- CHƯƠNG TRÌNH GIA SƯ HÀ NỘI ---");
            System.out.println("1. Đăng nhập | 2. Đăng ký | 0. Thoát");
            System.out.print("➤ Chọn: ");
            String choice = sc.nextLine();
            if (choice.equals("0")) break;

            if (choice.equals("1")) handleLogin();
            else if (choice.equals("2")) handleRegister();
        }
    }

    private static void handleLogin() {
        // QUAN TRỌNG: Cập nhật dữ liệu từ file trước khi kiểm tra đăng nhập
        DataService.loadData();

        System.out.println("\n--- ĐĂNG NHẬP ---");
        System.out.print("Username: "); String user = sc.nextLine().trim();
        System.out.print("Password: "); String pass = sc.nextLine().trim();

        User current = null;
        for (User u : DataService.allUsers) {
            // So sánh username không phân biệt hoa thường
            if (u.getUsername().equalsIgnoreCase(user) && u.getPassword().equals(pass)) {
                current = u;
                break;
            }
        }

        if (current != null) {
            System.out.println("✅ Đăng nhập thành công! Chào " + current.getFullName());
            session(current);
        } else {
            System.out.println("❌ Sai tài khoản hoặc mật khẩu!");
        }
    }

    private static void session(User user) {
        while (true) {
            // Luôn làm mới dữ liệu hệ thống từ file
            DataService.loadData();

            // Đồng bộ đối tượng user hiện tại với dữ liệu vừa nạp từ file
            for (User updatedUser : DataService.allUsers) {
                if (updatedUser.getId().equals(user.getId())) {
                    user = updatedUser;
                    break;
                }
            }

            user.displayMenu();
            System.out.print("➤ Chọn: ");
            String opt = sc.nextLine();

            if (user.isExit(opt)) {
                DataService.saveData(); // Lưu trước khi thoát phiên
                break;
            }

            execute(user, opt);

            // Lưu lại sau mỗi hành động thực thi thành công
            DataService.saveData();
        }
    }

    private static void execute(User u, String opt) {
        try {
            int c = Integer.parseInt(opt);
            if (u instanceof Parent p) {
                switch (c) {
                    case 1 -> ParentService.searchAndRequest(sc, p);
                    case 2 -> ParentService.createPost(sc, p.getId());
                    case 3 -> ParentService.managePosts(sc, p);
                    case 5 -> ParentService.classManagement(sc, p);
                }
            } else if (u instanceof Tutor t) {
                switch (c) {
                    case 1 -> TutorService.searchAndApply(sc, t);
                    case 2 -> TutorService.manageWallet(sc, t);
                    case 3 -> TutorService.teachingManagement(sc, t);
                    case 4 -> TutorService.handleInvitations(sc, t);
                }
            } else if (u instanceof Admin a) {
                switch (c) {
                    case 1 -> AdminService.manageTutors(sc, a);
                    case 2 -> AdminService.resolveComplaints(sc, a);
                    case 3 -> AdminService.showRevenue();
                }
            }
        } catch (Exception e) { System.out.println("❌ Lỗi: " + e.getMessage()); }
    }

    private static void handleRegister() {
        // Nạp dữ liệu để kiểm tra trùng lặp username
        DataService.loadData();

        System.out.println("\n--- ĐĂNG KÝ MỚI ---");
        System.out.print("1. Phụ huynh | 2. Gia sư: ");
        String role = sc.nextLine();
        System.out.print("Username: "); String u = sc.nextLine().trim();

        // Kiểm tra username tồn tại
        for (User existing : DataService.allUsers) {
            if (existing.getUsername().equalsIgnoreCase(u)) {
                System.out.println("❌ Tên đăng nhập đã tồn tại!");
                return;
            }
        }

        System.out.print("Pass: "); String p = sc.nextLine();
        System.out.print("Tên: "); String n = sc.nextLine();
        System.out.print("SĐT: "); String ph = sc.nextLine();

        String id = "U" + System.currentTimeMillis();
        User newUser;
        if (role.equals("1")) {
            newUser = new Parent(id, u, p, n, ph, "PARENT");
        } else {
            System.out.print("Môn: "); String sub = sc.nextLine();
            System.out.print("Quận: "); String ar = sc.nextLine();
            newUser = new Tutor(id, u, p, n, ph, "TUTOR", sub, ar);
        }

        DataService.allUsers.add(newUser);

        // QUAN TRỌNG: Lưu xuống file ngay để các cửa sổ khác có thể thấy
        DataService.saveData();
        System.out.println("✅ Đăng ký thành công! Bạn có thể đăng nhập ngay.");
    }
}