package com.tutorfinder;

import com.tutorfinder.data.Database;
import com.tutorfinder.managers.*;
import com.tutorfinder.model.*;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static User loggedUser = null; // Lưu tài khoản đang đăng nhập

    // Khởi tạo các Manager
    static AuthManager auth = new AuthManager();
    static ParentManager pMan = new ParentManager(sc);
    static TutorManager tMan = new TutorManager(sc);
    static AdminManager aMan = new AdminManager(sc);

    public static void main(String[] args) {
        while (true) {
            Database.load(); // LOAD
            if (loggedUser != null) {
                for (User u : Database.getInstance().users) {
                    if (u.getId().equals(loggedUser.getId())) {
                        loggedUser = u; // Cập nhật lại chính user
                        break;
                    }
                }
            }

            if (loggedUser == null) {
                System.out.println("\n=== TÌM GIA SƯ ONLINE ===");
                System.out.println("1. Đăng nhập");
                System.out.println("2. Đăng ký");
                System.out.println("0. Thoát");
                System.out.print("Chọn thao tác: ");
                String c = sc.nextLine();

                if (c.equals("1")) login();
                else if (c.equals("2")) register();
                else if (c.equals("0")) {
                    break;
                }
            } else {
                if (loggedUser instanceof Parent) showParent();
                else if (loggedUser instanceof Tutor) showTutor();
                else if (loggedUser instanceof Admin) showAdmin();
            }
        }
    }


    // CHỨC NĂNG ĐĂNG NHẬP
    static void login() {
        System.out.println("\n--- ĐĂNG NHẬP (Nhập 0 để quay lại) ---");
        System.out.print("Tên đăng nhập: ");
        String u = sc.nextLine();
        if (u.equals("0") || u.isEmpty()) return;

        System.out.print("Mật khẩu: ");
        String p = sc.nextLine();
        if (p.equals("0") || p.isEmpty()) return;

        loggedUser = auth.login(u, p);
        if (loggedUser == null) {
            System.out.println("LỖI: Sai tài khoản hoặc mật khẩu!");
        } else {
            System.out.println("Đăng nhập thành công!");
        }
    }

    // CHỨC NĂNG ĐĂNG KÝ
    static void register() {
        System.out.println("\n--- ĐĂNG KÝ TÀI KHOẢN ---");
        System.out.print("Bạn là ai? (1. Phụ huynh | 2. Gia sư): ");
        String r = sc.nextLine();

        // Nút thoát
        if (r.equals("0") || r.isEmpty()) return;
        if (!r.equals("1") && !r.equals("2")) {
            System.out.println("Lựa chọn không hợp lệ!");
            return;
        }

        System.out.print("Tên đăng nhập: ");
        String u = sc.nextLine();
        if (u.equals("0") || u.isEmpty()) return;

        System.out.print("Mật khẩu: "); String p = sc.nextLine();
        if (p.equals("0") || p.isEmpty()) return;
        System.out.print("Họ và Tên thật: "); String n = sc.nextLine();
        if (n.equals("0") || n.isEmpty()) return;
        System.out.print("Số điện thoại: "); String ph = sc.nextLine();
        if (ph.equals("0") || ph.isEmpty()) return;

        boolean ok = false;
        if (r.equals("1")) {
            ok = auth.registerParent(u, p, n, ph);
        } else {
            System.out.print("Môn dạy: "); String sub = sc.nextLine();
            if (sub.equals("0") || sub.isEmpty()) return;
            System.out.print("Lớp dạy: "); String gr = sc.nextLine();
            if (gr.equals("0") || gr.isEmpty()) return;
            System.out.print("khu vực: "); String ar = sc.nextLine();
            if (ar.equals("0") || ar.isEmpty()) return;
            ok = auth.registerTutor(u, p, n, ph, sub, gr, ar);
        }

        if (ok) System.out.println(" Đăng ký thành công.");
        else System.out.println("LỖI: Tên đăng nhập đã tồn tại!");
    }


    // CÁC MENU ĐIỀU HƯỚNG
    static void showParent() {
        System.out.println("\n--- MENU PHỤ HUYNH ---");
        System.out.println("1. Tìm Gia sư");
        System.out.println("2. Đăng bài tìm gia sư");
        System.out.println("3. Các bài đăng của tôi");
        System.out.println("4. Đổi mật khẩu ");
        System.out.println("0. Đăng xuất");
        System.out.print("Chọn: ");

        switch (sc.nextLine()) {
            case "1": pMan.findTutors(); break;
            case "2": pMan.createJobPost((Parent)loggedUser); break;
            case "3": pMan.manageMyPosts((Parent)loggedUser); break;
            case "4": auth.changePassword(loggedUser); break;
            case "0": loggedUser = null; break; // Xóa session = Đăng xuất
        }
    }

    static void showTutor() {
        Tutor t = (Tutor) loggedUser;
        String st = (t.getStatus() == 1) ? "ĐÃ DUYỆT" : "CHỜ DUYỆT";
        System.out.println("\n--- MENU GIA SƯ [" + st + "] ---");
        System.out.println("1. Tìm bài đăng ");
        System.out.println("2. các bài đã đăng kí");
        System.out.println("3. xem đánh giá");
        System.out.println("4. sửa hồ sơ");
        System.out.println("5. Đổi mật khẩu");
        System.out.println("0. Đăng xuất");
        System.out.print("Chọn: ");

        switch (sc.nextLine()) {
            case "1":
                if(t.getStatus() == 1) tMan.findJobPosts(t);
                else System.out.println("Tài khoản chưa được Admin duyệt, không thể tìm lớp!");
                break;

            case "2": tMan.manageMyRegistrations(t); break; // Gọi hàm mới tạo
            case "3": tMan.viewReviews(t); break;
            case "4": tMan.editProfile(t); break;
            case "5": auth.changePassword(loggedUser); break;
            case "0": loggedUser = null; break;
        }
    }

    static void showAdmin() {
        System.out.println("\n--- MENU ADMIN ---");
        System.out.println("1. Duyệt tài khoản Gia sư");
        System.out.println("2. Duyệt bài đăng");
        System.out.println("3. Tài khoản trên hệ thống");
        System.out.println("4. cấp lại mật khẩu cho người dùng");
        System.out.println("0. Đăng xuất");
        System.out.print("Chọn: ");

        switch (sc.nextLine()) {
            case "1": aMan.manageTutors(); break;
            case "2": aMan.manageJobPosts(); break;
            case "3": aMan.viewAllAccounts(); break;
            case "4": aMan.resetUserPassword(); break;
            case "0": loggedUser = null; break;
        }
    }
}