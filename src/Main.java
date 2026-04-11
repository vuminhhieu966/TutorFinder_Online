import com.tutorfinder.model.*;
import com.tutorfinder.service.DataService;
import com.tutorfinder.service.ParentService;
import java.util.Scanner;
import com.tutorfinder.service.TutorService;

public class Main {
    public static void main(String[] args) {
        // 1. Khởi tạo dữ liệu mẫu
        DataService.initData();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==============================================");
            System.out.println("     CHÀO MỪNG ĐẾN VỚI TUTORFINDER HÀ NỘI");
            System.out.println("==============================================");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký thành viên");
            System.out.println("0. Thoát chương trình");
            System.out.print("Lựa chọn của bạn: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());

                if (choice == 1) {
                    handleLogin(sc);
                } else if (choice == 2) {
                    System.out.println("Tính năng Đăng ký đang được phát triển...");
                } else if (choice == 0) {
                    System.out.println("Cảm ơn bạn đã sử dụng dịch vụ. Tạm biệt!");
                    break;
                } else {
                    System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng chỉ nhập số!");
            }
        }
    }

    // Logic xử lý Đăng nhập và điều hướng Menu con
    public static void handleLogin(Scanner sc) {
        System.out.println("\n--- ĐĂNG NHẬP HỆ THỐNG ---");
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        User loggedInUser = null;

        // Tìm kiếm User trong kho DataService
        for (User u : DataService.allUsers) {
            if (u.getUsername().equals(user) && u.getPassword().equals(pass)) {
                loggedInUser = u;
                break;
            }
        }

        if (loggedInUser != null) {
            System.out.println("\n✅ Đăng nhập thành công! Chào mừng " + loggedInUser.getFullName());

            // Vòng lặp giữ người dùng ở trong Menu của họ cho đến khi chọn Đăng xuất
            boolean isSessionActive = true;
            while (isSessionActive) {
                loggedInUser.displayMenu(); // Tính Đa hình: Tự hiện Menu đúng vai trò
                System.out.print("Lựa chọn tính năng (0 để đăng xuất): ");

                try {
                    int subChoice = Integer.parseInt(sc.nextLine());

                    if (subChoice == 0) {
                        System.out.println("Đang đăng xuất...");
                        isSessionActive = false;
                    } else {
                        // Điều hướng xử lý dựa trên Vai trò (Role)
                        handleUserActions(sc, loggedInUser, subChoice);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Lỗi: Vui lòng nhập số!");
                }
            }
        } else {
            System.out.println("❌ Sai tài khoản hoặc mật khẩu!");
        }
    }

    // Hàm điều phối các hành động cụ thể của từng loại User
    public static void handleUserActions(Scanner sc, User user, int choice) {
        String role = user.getRole();

        switch (role) {
            case "PARENT":
                if (choice == 2) { // Mục số 2 trong Menu Parent là Đăng bài
                    ParentService.createPost(sc, user.getId());
                } else {
                    System.out.println("Tính năng đang được cập nhật...");
                }
                break;

            case "TUTOR":
                if (choice == 1) {
                    // Mục 1 trong Menu Tutor: Tìm lớp học
                    System.out.println("1. Xem tất cả");
                    System.out.println("2. Tìm theo quận");
                    System.out.print("Lựa chọn: ");
                    int searchChoice = Integer.parseInt(sc.nextLine());

                    if (searchChoice == 1) {
                        TutorService.showAllPosts();
                    } else if (searchChoice == 2) {
                        TutorService.searchByDistrict(sc);
                    }
                } else if (choice == 2) {
                    // Lựa chọn số 2 trong Menu Gia sư là Nạp tiền
                    // Chúng ta ép kiểu (cast) từ User sang Tutor để truy cập được vào Ví tiền
                    TutorService.depositMoney(sc, (Tutor) user);

                } else if (choice == 3) { // THÊM MỚI Ở ĐÂY
                    TutorService.applyPost(sc, (Tutor) user);
                }
                break;

            case "ADMIN":
                System.out.println("Tính năng cho Admin đang được phát triển...");
                break;

            default:
                System.out.println("Vai trò không xác định!");
                break;
        }
    }
}