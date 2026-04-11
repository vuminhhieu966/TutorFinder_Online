package com.tutorfinder.model;

public abstract class User {
    private String id;
    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String role; // Để phân biệt: "PARENT", "TUTOR", "ADMIN"

    // Constructor: Hàm khởi tạo để tạo ra một User mới
    public User(String id, String username, String password, String fullName, String phoneNumber, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Getter: Để các lớp khác có thể lấy thông tin (vì thuộc tính đang để private - tính Đóng gói)
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRole() { return role; }

    // Setter: Cho phép đổi mật khẩu
    public void setPassword(String password) { this.password = password; }

    // Phương thức trừu tượng: Ép các lớp con sau này phải có hàm hiển thị Menu riêng
    public abstract void displayMenu();
}