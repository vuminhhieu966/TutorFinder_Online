package com.tutorfinder.model;

// Sử dụng abstract để không cho phép tạo đối tượng "User" chung chung
public abstract class User {
    private String id;
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String role; // ADMIN, PARENT, hoặc TUTOR

    // Constructor để các lớp con gọi tới (Super)
    public User(String id, String username, String password, String fullName, String phone, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
    }

    // Phương thức trừu tượng: Mỗi loại User sẽ có cách hiện Menu khác nhau
    public abstract void displayMenu();

    // Các Getter và Setter (Encapsulation - Đóng gói)
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
    public String getId() { return id; }

    public void setPassword(String password) { this.password = password; }
}