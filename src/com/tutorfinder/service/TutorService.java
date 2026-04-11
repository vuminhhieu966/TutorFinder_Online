package com.tutorfinder.service;

import com.tutorfinder.model.Post;
import com.tutorfinder.model.Tutor;

import java.util.Scanner;

public class TutorService {

    // 1. Hàm hiển thị toàn bộ các lớp đang mở
    public static void showAllPosts() {
        System.out.println("\n--- DANH SÁCH LỚP HỌC ĐANG TÌM GIA SƯ ---");
        if (DataService.activePosts.isEmpty()) {
            System.out.println("Hiện tại chưa có bài đăng nào.");
            return;
        }
        for (Post p : DataService.activePosts) {
            if (p.getStatus().equals("OPEN")) {
                p.displayPost();
            }
        }
    }

    // 2. Hàm tìm kiếm lớp theo Quận
    public static void searchByDistrict(Scanner sc) {
        System.out.print("Nhập mã quận bạn muốn tìm (1-12): ");
        int districtId = Integer.parseInt(sc.nextLine());
        boolean found = false;

        System.out.println("\n--- KẾT QUẢ TÌM KIẾM TẠI QUẬN " + districtId + " ---");
        for (Post p : DataService.activePosts) {
            if (p.getDistrictId() == districtId && p.getStatus().equals("OPEN")) {
                p.displayPost();
                found = true;
            }
        }
        if (!found) System.out.println("Không tìm thấy lớp nào ở khu vực này.");
    }
    // Hàm nạp tiền vào ví cho Gia sư
    public static void depositMoney(Scanner sc, Tutor tutor) {
        System.out.println("\n--- NẠP TIỀN VÀO VÍ ---");
        System.out.print("Nhập số tiền muốn nạp (VNĐ): ");
        try {
            double amount = Double.parseDouble(sc.nextLine());
            if (amount > 0) {
                // Cộng tiền vào thuộc tính balance của đối tượng tutor
                tutor.setBalance(tutor.getBalance() + amount);
                System.out.println("✅ Nạp thành công! Số dư hiện tại: " + tutor.getBalance() + " VNĐ");
            } else {
                System.out.println("❌ Số tiền phải lớn hơn 0!");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Vui lòng nhập số tiền hợp lệ (chỉ nhập số)!");
        }
    }
    // hàm đăng kí nhận lớp
    public static void applyPost(Scanner sc, Tutor tutor) {
        System.out.print("\nNhập mã bài đăng (ID) bạn muốn nhận: ");
        String id = sc.nextLine();
        Post foundPost = null;

        // 1. Tìm bài đăng theo ID
        for (Post p : DataService.activePosts) {
            if (p.getPostId().equals(id) && p.getStatus().equals("OPEN")) {
                foundPost = p;
                break;
            }
        }

        if (foundPost == null) {
            System.out.println("❌ Không tìm thấy bài đăng hoặc bài đã có người nhận!");
            return;
        }

        // 2. Tính phí nhận lớp (2 buổi)
        double feeRequired = foundPost.getFeePerLesson() * 2;
        System.out.println("Lớp này yêu cầu phí nhận lớp là: " + feeRequired + " VNĐ");
        System.out.println("Số dư ví hiện tại của bạn: " + tutor.getBalance() + " VNĐ");

        // 3. Kiểm tra ví và trừ tiền
        if (tutor.getBalance() >= feeRequired) {
            tutor.setBalance(tutor.getBalance() - feeRequired);
            foundPost.setStatus("CLOSED"); // Chốt lớp, không cho ai thấy nữa
            System.out.println("✅ CHÚC MỪNG! Bạn đã nhận lớp thành công.");
            System.out.println("Số dư còn lại: " + tutor.getBalance() + " VNĐ");
        } else {
            System.out.println("❌ Tài khoản không đủ tiền! Vui lòng nạp thêm tối thiểu " + (feeRequired - tutor.getBalance()) + " VNĐ.");
        }
    }
}