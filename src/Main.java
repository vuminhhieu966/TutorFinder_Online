import com.tutorfinder.model.*;
import com.tutorfinder.service.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Khởi tạo dữ liệu mẫu từ DataService
        DataService.initData();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==============================================");
            System.out.println("     HỆ THỐNG KẾT NỐI GIA SƯ TUTORFINDER");
            System.out.println("==============================================");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký thành viên mới");
            System.out.println("0. Thoát");
            System.out.print("Lựa chọn của bạn: ");

            try {
                int choice = Integer.parseInt(sc.nextLine());

                if (choice == 1) {
                    handleLogin(sc);
                } else if (choice == 2) {
                    handleRegister(sc);
                } else if (choice == 0) {
                    System.out.println("Cảm ơn bạn đã sử dụng TutorFinder. Tạm biệt!");
                    break;
                }
            } catch (Exception e) {
                System.out.println("Lỗi: Vui lòng nhập đúng định dạng số!");
            }
        }
    }

    // --- LOGIC ĐĂNG KÝ ---
    public static void handleRegister(Scanner sc) {
        System.out.println("\n--- ĐĂNG KÝ TÀI KHOẢN ---");
        System.out.println("1. Bạn là Phụ huynh | 2. Bạn là Gia sư");
        System.out.print("Lựa chọn: ");
        int type = Integer.parseInt(sc.nextLine());

        System.out.print("Username mới: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();
        System.out.print("Họ và tên: ");
        String name = sc.nextLine();
        System.out.print("Số điện thoại: ");
        String phone = sc.nextLine();

        String id = "U" + (DataService.allUsers.size() + 1);

        if (type == 1) {
            DataService.allUsers.add(new Parent(id, user, pass, name, phone, "PARENT"));
            System.out.println("✅ Đăng ký Phụ huynh thành công!");
        } else {
            System.out.print("Môn học sở trường: ");
            String sub = sc.nextLine();
            System.out.print("Khu vực dạy (Quận): ");
            String area = sc.nextLine();
            DataService.allUsers.add(new Tutor(id, user, pass, name, phone, "TUTOR", sub, area));
            System.out.println("✅ Đăng ký Gia sư thành công! (Chờ Admin duyệt)");
        }
    }

    // --- LOGIC ĐĂNG NHẬP & ĐIỀU HƯỚNG ---
    public static void handleLogin(Scanner sc) {
        System.out.print("\nUsername: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        User loggedInUser = null;
        for (User u : DataService.allUsers) {
            if (u.getUsername().equals(user) && u.getPassword().equals(pass)) {
                loggedInUser = u;
                break;
            }
        }

        if (loggedInUser != null) {
            System.out.println("✅ Đăng nhập thành công! Chào " + loggedInUser.getFullName());

            boolean session = true;
            while (session) {
                loggedInUser.displayMenu(); // Đa hình: Gọi menu tương ứng Role
                System.out.print("Lựa chọn tính năng (0 để đăng xuất): ");
                int choice = Integer.parseInt(sc.nextLine());

                if (choice == 0) {
                    session = false;
                } else {
                    executeAction(sc, loggedInUser, choice);
                }
            }
        } else {
            System.out.println("❌ Sai tài khoản hoặc mật khẩu!");
        }
    }

    // --- THỰC THI HÀNH ĐỘNG THEO VAI TRÒ ---
    public static void executeAction(Scanner sc, User user, int choice) {
        if (user instanceof Parent) {
            if (choice == 1) System.out.println("Tính năng xem lịch sử đang cập nhật...");
            else if (choice == 2) ParentService.createPost(sc, user.getId());
        }
        else if (user instanceof Tutor) {
            Tutor t = (Tutor) user;
            if (choice == 1) {
                System.out.println("1. Xem tất cả | 2. Tìm theo quận");
                int sub = Integer.parseInt(sc.nextLine());
                if (sub == 1) TutorService.showAllPosts();
                else TutorService.searchByDistrict(sc);
            }
            else if (choice == 2) TutorService.depositMoney(sc, t);
            else if (choice == 3) TutorService.showTutorProfile(t);
            else if (choice == 4) TutorService.applyPost(sc, t);
        }
        else if (user instanceof Admin) {
            if (choice == 1) AdminService.approveTutors(sc);
            else System.out.println("Tính năng đang cập nhật...");
        }
    }
}