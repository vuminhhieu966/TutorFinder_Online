package com.tutorfinder;

import com.tutorfinder.data.Database;
import com.tutorfinder.managers.*;
import com.tutorfinder.model.*;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static User loggedUser = null; // Lưu tài khoản đang đăng nhập

    // Khởi tạo các Manager xử lý logic
    static AuthManager auth = new AuthManager();
    static ParentManager pMan = new ParentManager(sc);
    static TutorManager tMan = new TutorManager(sc);
    static AdminManager aMan = new AdminManager(sc);

    public static void main(String[] args) {
        while (true) {
            Database.load(); // LUÔN LOAD: Để thấy thay đổi từ cửa sổ khác

            if (loggedUser == null) {
                System.out.println("\n=== HỆ THỐNG GIA SƯ HÀ NỘI ===");
                System.out.println("1. Đăng nhập");
                System.out.println("2. Đăng ký");
                System.out.println("0. Thoát chương trình");
                System.out.print("Chọn thao tác: ");
                String c = sc.nextLine();

                if (c.equals("1")) login();
                else if (c.equals("2")) register();
                else if (c.equals("0")) {
                    System.out.println("Cảm ơn bạn đã sử dụng hệ thống!");
                    break;
                }
            } else {
                // Phân quyền Menu dựa trên class của User
                if (loggedUser instanceof Parent) showParent();
                else if (loggedUser instanceof Tutor) showTutor();
                else if (loggedUser instanceof Admin) showAdmin();
            }
        }
    }

    // ==============================================
    // CHỨC NĂNG ĐĂNG NHẬP (CÓ NÚT THOÁT)
    // ==============================================
    static void login() {
        System.out.println("\n--- ĐĂNG NHẬP (Nhập 0 để quay lại) ---");
        System.out.print("Tên đăng nhập: ");
        String u = sc.nextLine();

        // Nút thoát hiểm: Nếu gõ 0 thì thoát ngay hàm login() về menu ngoài
        if (u.equals("0")) return;

        System.out.print("Mật khẩu: ");
        String p = sc.nextLine();

        loggedUser = auth.login(u, p);
        if (loggedUser == null) {
            System.out.println("LỖI: Sai tài khoản hoặc mật khẩu!");
        } else {
            System.out.println("Đăng nhập thành công!");
        }
    }

    // ==============================================
    // CHỨC NĂNG ĐĂNG KÝ (CÓ NÚT THOÁT)
    // ==============================================
    static void register() {
        System.out.println("\n--- ĐĂNG KÝ TÀI KHOẢN (Nhập 0 để quay lại) ---");
        System.out.print("Bạn là ai? (1. Phụ huynh | 2. Gia sư): ");
        String r = sc.nextLine();

        // Nút thoát hiểm ngay từ bước chọn vai trò
        if (r.equals("0")) return;
        if (!r.equals("1") && !r.equals("2")) {
            System.out.println("Lựa chọn không hợp lệ!");
            return;
        }

        System.out.print("Tên đăng nhập muốn tạo: ");
        String u = sc.nextLine();
        // Thoát hiểm nếu đang gõ dở mà đổi ý
        if (u.equals("0")) return;

        System.out.print("Mật khẩu: "); String p = sc.nextLine();
        System.out.print("Họ và Tên thật: "); String n = sc.nextLine();
        System.out.print("Số điện thoại: "); String ph = sc.nextLine();

        boolean ok = false;
        if (r.equals("1")) {
            ok = auth.registerParent(u, p, n, ph);
        } else {
            System.out.print("Môn dạy (VD: Toán, Lý): "); String sub = sc.nextLine();
            System.out.print("Lớp dạy (VD: Lớp 10, Lớp 12): "); String gr = sc.nextLine();
            System.out.print("Quận khu vực (VD: Cầu Giấy): "); String ar = sc.nextLine();
            ok = auth.registerTutor(u, p, n, ph, sub, gr, ar);
        }

        if (ok) System.out.println("Chúc mừng! Đăng ký thành công.");
        else System.out.println("LỖI: Tên đăng nhập này đã có người sử dụng!");
    }

    // ==============================================
    // CÁC MENU ĐIỀU HƯỚNG
    // ==============================================
    static void showParent() {
        System.out.println("\n--- MENU PHỤ HUYNH ---");
        System.out.println("1. Tìm Gia sư (Xem SĐT / Đánh giá)");
        System.out.println("2. Đăng bài tìm gia sư");
        System.out.println("3. Các bài đăng của tôi");
        System.out.println("0. Đăng xuất");
        System.out.print("Chọn: ");

        switch (sc.nextLine()) {
            case "1": pMan.findTutors(); break;
            case "2": pMan.createJobPost((Parent)loggedUser); break;
            case "3": pMan.manageMyPosts((Parent)loggedUser); break;
            case "0": loggedUser = null; break; // Xóa session = Đăng xuất
        }
    }

    static void showTutor() {
        Tutor t = (Tutor) loggedUser;
        String st = (t.getStatus() == 1) ? "ĐÃ DUYỆT" : "CHỜ DUYỆT";
        System.out.println("\n--- MENU GIA SƯ [" + st + "] ---");
        System.out.println("1. Tìm lớp đăng ký dạy");
        System.out.println("2. Xem chỉ số đánh giá của tôi");
        System.out.println("0. Đăng xuất");
        System.out.print("Chọn: ");

        switch (sc.nextLine()) {
            case "1":
                if(t.getStatus() == 1) tMan.findJobPosts(t);
                else System.out.println("Tài khoản chưa được Admin duyệt, không thể tìm lớp!");
                break;
            case "2": tMan.viewReviews(t); break;
            case "0": loggedUser = null; break;
        }
    }

    static void showAdmin() {
        System.out.println("\n--- MENU ADMIN ---");
        System.out.println("1. Duyệt / Khóa Gia sư");
        System.out.println("2. Duyệt bài đăng tìm Gia sư");
        System.out.println("0. Đăng xuất");
        System.out.print("Chọn: ");

        switch (sc.nextLine()) {
            case "1": aMan.manageTutors(); break;
            case "2": aMan.manageJobPosts(); break;
            case "0": loggedUser = null; break;
        }
    }
}