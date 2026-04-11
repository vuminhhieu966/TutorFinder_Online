package com.tutorfinder.model;
import java.util.Scanner;
import com.tutorfinder.service.DataService;

public class Tutor extends User {
    private String subject;     // Môn học thế mạnh
    private String area;        // Quận huyện ở Hà Nội
    private double balance;     // Ví tiền (mặc định ban đầu là 0)
    private boolean isApproved; // Admin đã duyệt hồ sơ chưa?

    public Tutor(String id, String username, String password, String fullName, String phoneNumber,
                 String subject, String area) {
        // super: Gọi hàm khởi tạo của lớp cha (User)
        super(id, username, password, fullName, phoneNumber, "TUTOR");
        this.subject = subject;
        this.area = area;
        this.balance = 0.0;
        this.isApproved = false; // Mới đăng ký thì chưa được duyệt
    }

    // Các hàm xử lý ví tiền
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public boolean isApproved() { return isApproved; }
    public void setApproved(boolean approved) { isApproved = approved; }

    public String getSubject() { return subject; }
    public String getArea() { return area; }



    @Override
    public void displayMenu() {
        System.out.println("\n--- MENU GIA SƯ: " + getFullName() + " ---");
        System.out.println("1. Tìm lớp học phù hợp (theo Quận/Môn)");
        System.out.println("2. Nạp tiền vào ví");
        System.out.println("3. Kiểm tra số dư & Lịch sử Apply");
        System.out.println("4. Gửi khiếu nại cho Admin");
        System.out.println("0. Đăng xuất");
    }
}