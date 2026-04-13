package com.tutorfinder;

import com.tutorfinder.data.DataStore;
import com.tutorfinder.managers.*;
import com.tutorfinder.model.*;

import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);

    // Khởi tạo các Trợ lý nghiệp vụ (Managers)
    private static AuthManager authManager = new AuthManager();
    private static ParentManager parentManager = new ParentManager(sc);
    private static TutorManager tutorManager = new TutorManager(sc);
    private static AdminManager adminManager = new AdminManager(sc);

    // Lưu người dùng đang đăng nhập hiện tại
    private static User loggedInUser = null;

    public static void main(String[] args) {
        System.out.println("Hệ thống Tutor Finder Online đang khởi động...");

        while (true) {
            // Lệnh cực kỳ quan trọng: Liên tục đọc dữ liệu mới nhất từ File
            // Nhờ lệnh này mà 2 cửa sổ Console sẽ nhìn thấy dữ liệu của nhau ngay lập tức!
            DataStore.loadData();

            // Cập nhật lại thông tin ví tiền, trạng thái của người đang đăng nhập
            if (loggedInUser != null) {
                User checkUser = DataStore.getInstance().getUserById(loggedInUser.getId());
                // Nếu bị Admin khóa, tự động văng ra ngoài
                if (checkUser == null || (checkUser instanceof Tutor && ((Tutor) checkUser).getStatus() == -1)) {
                    System.out.println("Tài khoản của bạn đã bị KHÓA bởi Admin! Đang tự động đăng xuất...");
                    loggedInUser = null;
                } else {
                    loggedInUser = checkUser;
                }
            }

            if (loggedInUser == null) {
                showInitialMenu();
            } else {
                String role = loggedInUser.getRole();
                if (role.equals("PARENT")) {
                    showParentMenu();
                } else if (role.equals("TUTOR")) {
                    showTutorMenu();
                } else if (role.equals("ADMIN")) {
                    showAdminMenu();
                }
            }
        }
    }

    // ==========================================
    // MENU BAN ĐẦU & XÁC THỰC
    // ==========================================
    private static void showInitialMenu() {
        System.out.println("\n=========== TUTOR FINDER ONLINE ===========");
        System.out.println("1. Đăng nhập");
        System.out.println("2. Đăng ký");
        System.out.println("0. Đăng xuất (Thoát chương trình)");
        System.out.print("Vui lòng chọn: ");
        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1": handleLogin(); break;
            case "2": handleRegister(); break;
            case "0":
                System.out.println("Cảm ơn bạn đã sử dụng hệ thống!");
                System.exit(0);
            default: System.out.println("Lựa chọn không hợp lệ!");
        }
    }

    private static void handleLogin() {
        System.out.print("Tên đăng nhập: "); String u = sc.nextLine();
        System.out.print("Mật khẩu: "); String p = sc.nextLine();
        loggedInUser = authManager.login(u, p);
        if (loggedInUser == null) {
            System.out.println("Đăng nhập thất bại! Sai tên đăng nhập hoặc mật khẩu.");
        } else {
            System.out.println("Đăng nhập thành công!");
        }
    }

    private static void handleRegister() {
        System.out.println("\n--- ĐĂNG KÝ TÀI KHOẢN ---");
        System.out.println("1. Phụ huynh | 2. Gia sư");
        System.out.print("Chọn loại tài khoản: "); String role = sc.nextLine().trim();
        System.out.print("Tên đăng nhập: "); String u = sc.nextLine();
        System.out.print("Mật khẩu: "); String p = sc.nextLine();
        System.out.print("Số điện thoại: "); String phone = sc.nextLine();

        boolean success = false;
        if (role.equals("1")) {
            success = authManager.registerParent(u, p, phone);
        } else if (role.equals("2")) {
            System.out.print("Môn học giảng dạy: "); String sub = sc.nextLine();
            success = authManager.registerTutor(u, p, phone, sub);
        } else {
            System.out.println("Loại tài khoản không hợp lệ!");
            return;
        }

        if (success) {
            System.out.println("Đăng ký thành công! Bạn có thể đăng nhập ngay.");
            if (role.equals("2")) System.out.println("Lưu ý: Gia sư mới cần chờ Admin duyệt.");
        } else {
            System.out.println("Đăng ký thất bại! Tên đăng nhập đã tồn tại.");
        }
    }

    // ==========================================
    // MENU PHỤ HUYNH
    // ==========================================
    private static void showParentMenu() {
        Parent p = (Parent) loggedInUser;
        System.out.println("\n=== MENU PHỤ HUYNH: " + p.getUsername() + " ===");
        System.out.println("1. Tìm gia sư (Gửi yêu cầu)");
        System.out.println("2. Đăng bài tìm gia sư");
        System.out.println("3. Ví cá nhân (Nạp/Rút)");
        System.out.println("4. Quản lý Đơn yêu cầu đã gửi");
        System.out.println("5. Quản lý Bài đăng (Chọn gia sư)");
        System.out.println("6. Các Lớp học (Vào học, Đánh giá, Khiếu nại)");
        System.out.println("0. Đăng xuất");
        System.out.print("Chọn thao tác: ");

        switch (sc.nextLine().trim()) {
            case "1": parentManager.findTutors(p); break;
            case "2": parentManager.createJobPost(p); break;
            case "3": handleWallet(p); break;
            case "4": parentManager.manageRequests(p); break;
            case "5": parentManager.manageJobPosts(p); break;
            case "6": parentManager.manageCourses(p); break;
            case "0": loggedInUser = null; break;
            default: System.out.println("Lựa chọn không hợp lệ!");
        }
    }

    // ==========================================
    // MENU GIA SƯ
    // ==========================================
    private static void showTutorMenu() {
        Tutor t = (Tutor) loggedInUser;
        String st = t.getStatus() == 1 ? "ĐÃ DUYỆT" : "CHỜ DUYỆT";
        System.out.println("\n=== MENU GIA SƯ: " + t.getUsername() + " [" + st + "] ===");
        System.out.println("1. Tìm lớp học (Từ bài đăng của PH)");
        System.out.println("2. Ví cá nhân (Nạp/Rút)");
        System.out.println("3. Thư mời từ Phụ huynh (Nhận lớp)");
        System.out.println("4. Các Lớp học (Mở lớp, Đóng lớp)");
        System.out.println("5. Xem đánh giá của tôi");
        System.out.println("0. Đăng xuất");
        System.out.print("Chọn thao tác: ");
        String choice = sc.nextLine().trim();

        // Ràng buộc tính năng khi chưa được duyệt
        if (t.getStatus() == 0 && (choice.equals("1") || choice.equals("3") || choice.equals("4"))) {
            System.out.println("Tài khoản của bạn đang CHỜ DUYỆT, không thể sử dụng tính năng này!");
            return;
        }

        switch (choice) {
            case "1": tutorManager.findJobPosts(t); break;
            case "2": handleWallet(t); break;
            case "3": tutorManager.manageRequests(t); break;
            case "4": tutorManager.manageCourses(t); break;
            case "5":
                System.out.printf("Đánh giá: %.1f Sao | Số lượt: %d\n", t.getRating(), t.getReviewCount());
                break;
            case "0": loggedInUser = null; break;
            default: System.out.println("Lựa chọn không hợp lệ!");
        }
    }

    // ==========================================
    // MENU ADMIN
    // ==========================================
    private static void showAdminMenu() {
        System.out.println("\n=== MENU ADMIN ===");
        System.out.println("1. Quản lý Gia sư (Duyệt/Khóa)");
        System.out.println("2. Xử lý Khiếu nại từ Phụ huynh");
        System.out.println("3. Báo cáo Doanh thu Hệ thống");
        System.out.println("0. Đăng xuất");
        System.out.print("Chọn thao tác: ");

        switch (sc.nextLine().trim()) {
            case "1": adminManager.manageTutors(); break;
            case "2": adminManager.manageComplaints(); break;
            case "3": adminManager.showRevenue(); break;
            case "0": loggedInUser = null; break;
            default: System.out.println("Lựa chọn không hợp lệ!");
        }
    }

    // ==========================================
    // VÍ CÁ NHÂN (Dùng chung cho PH và GS)
    // ==========================================
    private static void handleWallet(User u) {
        System.out.println("\n--- VÍ CÁ NHÂN ---");
        System.out.println("Số dư hiện tại: " + u.getWalletBalance() + " VNĐ");
        System.out.println("1. Nạp tiền | 2. Rút tiền | 0. Thoát");
        System.out.print("Chọn thao tác: ");
        String c = sc.nextLine().trim();

        try {
            if (c.equals("1")) {
                System.out.print("Nhập số tiền muốn nạp (VNĐ): ");
                double amt = Double.parseDouble(sc.nextLine());
                u.addMoney(amt);
                DataStore.saveData(); // Cập nhật ngay lập tức
                System.out.println("Nạp thành công!");
            } else if (c.equals("2")) {
                System.out.print("Nhập số tiền muốn rút (VNĐ): ");
                double amt = Double.parseDouble(sc.nextLine());
                if (u.withdrawMoney(amt)) {
                    DataStore.saveData();
                    System.out.println("Rút thành công!");
                } else {
                    System.out.println("Số dư không đủ!");
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi: Vui lòng nhập số hợp lệ.");
        }
    }
}