import com.tutorfinder.model.*;
import com.tutorfinder.service.DataService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Nạp dữ liệu mẫu
        DataService.initData();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== CHÀO MỪNG ĐẾN VỚI TUTORFINDER HÀ NỘI =====");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký (Gia sư/Phụ huynh)");
            System.out.println("0. Thoát");
            System.out.print("Lựa chọn của bạn: ");
            int choice = Integer.parseInt(sc.nextLine());

            if (choice == 1) {
                handleLogin(sc);
            } else if (choice == 0) {
                System.out.println("Tạm biệt!");
                break;
            }
        }
    }

    // Logic xử lý Đăng nhập
    public static void handleLogin(Scanner sc) {
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        User loggedInUser = null;

        // Tìm kiếm trong kho dữ liệu
        for (User u : DataService.allUsers) {
            if (u.getUsername().equals(user) && u.getPassword().equals(pass)) {
                loggedInUser = u;
                break;
            }
        }

        if (loggedInUser != null) {
            System.out.println("Đăng nhập thành công! Chào " + loggedInUser.getFullName());
            // Hiển thị menu tương ứng với vai trò (Tính đa hình - Polymorphism)
            loggedInUser.displayMenu();
        } else {
            System.out.println("Sai tài khoản hoặc mật khẩu!");
        }
    }
}