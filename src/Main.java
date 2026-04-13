package com.tutorfinder;

import com.tutorfinder.model.*;
import com.tutorfinder.service.*;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // Nạp dữ liệu ngay khi mở app
        DataService.loadData();

        while (true) {
            // Luôn load lại dữ liệu ở đầu vòng lặp để đồng bộ với các cửa sổ khác
            DataService.loadData();

            System.out.println("\n========= HỆ THỐNG GIA SƯ ONLINE =========");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký");
            System.out.println("0. Thoát chương trình");
            System.out.println("===========================================");
            System.out.print("➤ Chọn chức năng: ");
            String choice = sc.nextLine().trim();

            if (choice.equals("0")) {
                System.out.println("👋 Tạm biệt! Dữ liệu đã được lưu an toàn.");
                break;
            }

            switch (choice) {
                case "1" -> handleLogin();
                case "2" -> handleRegister();
                default -> System.out.println("⚠️ Lựa chọn không hợp lệ!");
            }
        }
    }

    /**
     * Xử lý Đăng ký: Tự sinh ID p1, t1 và ghi vào accounts.txt
     */
    private static void handleRegister() {
        System.out.println("\n--- ĐĂNG KÝ TÀI KHOẢN MỚI ---");
        System.out.print("Chọn vai trò (1. Phụ huynh | 2. Gia sư): ");
        String roleOpt = sc.nextLine().trim();

        System.out.print("Username: "); String user = sc.nextLine().trim();

        // Kiểm tra trùng Username (không phân biệt hoa thường)
        for (User u : DataService.allUsers) {
            if (u.getUsername().equalsIgnoreCase(user)) {
                System.out.println("❌ Tên đăng nhập đã tồn tại! Thử tên khác.");
                return;
            }
        }

        System.out.print("Mật khẩu: "); String pass = sc.nextLine().trim();
        System.out.print("Họ và tên: "); String name = sc.nextLine().trim();
        System.out.print("Số điện thoại: "); String phone = sc.nextLine().trim();

        if (roleOpt.equals("1")) {
            // Sinh ID dạng p1, p2...
            String id = DataService.generateId("p");
            DataService.allUsers.add(new Parent(id, user, pass, name, phone, "PARENT"));
            System.out.println("✅ Đăng ký thành công Phụ huynh! ID của bạn là: " + id);
        } else if (roleOpt.equals("2")) {
            System.out.print("Môn dạy chuyên môn: "); String sub = sc.nextLine().trim();
            // Sinh ID dạng t1, t2...
            String id = DataService.generateId("t");
            DataService.allUsers.add(new Tutor(id, user, pass, name, phone, "TUTOR", sub));
            System.out.println("✅ Đăng ký thành công Gia sư! ID của bạn là: " + id);
        } else {
            System.out.println("❌ Vai trò không hợp lệ!");
            return;
        }

        // Lưu file nhị phân và xuất accounts.txt ngay lập tức
        DataService.saveData();
    }

    /**
     * Xử lý Đăng nhập: Kiểm tra trạng thái Gia sư (Duyệt/Khóa)
     */
    private static void handleLogin() {
        System.out.println("\n--- ĐĂNG NHẬP HỆ THỐNG ---");
        System.out.print("Username: "); String u = sc.nextLine().trim();
        System.out.print("Password: "); String p = sc.nextLine().trim();

        User loggedInUser = null;
        for (User user : DataService.allUsers) {
            // Đăng nhập không phân biệt hoa thường với Username
            if (user.getUsername().equalsIgnoreCase(u) && user.getPassword().equals(p)) {
                loggedInUser = user;
                break;
            }
        }

        if (loggedInUser == null) {
            System.out.println("❌ Sai tài khoản hoặc mật khẩu!");
            return;
        }

        // Kiểm tra riêng cho Gia sư: Nếu bị khóa thì không cho vào
        if (loggedInUser instanceof Tutor t) {
            if (t.getStatus().equals("LOCKED")) {
                System.out.println("🛑 TÀI KHOẢN BỊ KHÓA! Vui lòng liên hệ Admin để xử lý.");
                return;
            }
        }

        System.out.println("✅ Đăng nhập thành công! Chào " + loggedInUser.getFullName());
        enterSession(loggedInUser);
    }

    /**
     * Quản lý phiên làm việc sau khi đăng nhập
     */
    private static void enterSession(User user) {
        while (true) {
            // Cập nhật lại đối tượng user từ List chung (để lấy ví tiền, trạng thái mới nhất)
            DataService.loadData();
            for (User updated : DataService.allUsers) {
                if (updated.getId().equals(user.getId())) {
                    user = updated;
                    break;
                }
            }

            // Hiển thị Menu tương ứng với vai trò (đã định nghĩa trong mỗi Class Model)
            user.displayMenu();
            System.out.print("➤ Nhập lựa chọn (0 để Đăng xuất): ");
            String opt = sc.nextLine().trim();

            if (user.isExit(opt)) {
                System.out.println("🔒 Đã đăng xuất.");
                break;
            }

            // Đây là nơi gọi đến các Service xử lý (Sẽ viết ở bước sau)
            executeFeature(user, opt);

            // Sau mỗi hành động, tự động lưu dữ liệu
            DataService.saveData();
        }
    }

    /**
     * Điều phối các tính năng dựa trên vai trò
     */
    private static void executeFeature(User u, String opt) {
        if (u instanceof Parent p) {
            switch (opt) {
                case "1" -> ParentService.searchTutor(p);
                case "2" -> ParentService.createPost(p);
                case "3" -> ParentService.manageWallet(p);
                case "4" -> ParentService.manageRequests(p);
                case "5" -> ParentService.managePosts(p);
                case "6" -> ParentService.manageClasses(p);
            }
        } else if (u instanceof Tutor t) {
            switch (opt) {
                case "1" -> TutorService.findJob(t);
                case "2" -> TutorService.manageWallet(t);
                case "3" -> TutorService.manageAppliedClasses(t);
                case "4" -> TutorService.manageInvitations(t);
                case "5" -> TutorService.manageTeachingClasses(t);
                case "6" -> TutorService.viewReviews(t);
            }
        } else if (u instanceof Admin a) {
            switch (opt) {
                case "1" -> AdminService.manageTutors();
                case "2" -> AdminService.handleComplaints();
                case "3" -> AdminService.viewRevenue();
            }
        }
    }
}