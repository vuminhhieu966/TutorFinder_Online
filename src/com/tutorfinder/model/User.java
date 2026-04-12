package com.tutorfinder.model;

import java.io.Serializable;

public abstract class User implements Serializable {
    // ID phiên bản để đảm bảo các Console đồng bộ dữ liệu file với nhau
    private static final long serialVersionUID = 1L;

    private String id;
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String role;

    public User(String id, String username, String password, String fullName, String phone, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
    }

    // Mỗi vai trò sẽ có một menu riêng
    public abstract void displayMenu();

    // --- TIỆN ÍCH DÙNG CHUNG TOÀN HỆ THỐNG ---

    /**
     * Kiểm tra người dùng có muốn thoát/quay lại không
     */
    public boolean isExit(String input) {
        if (input == null) return false;
        String trimInput = input.trim();
        return trimInput.equalsIgnoreCase("0") || trimInput.equalsIgnoreCase("exit");
    }

    /**
     * So sánh chuỗi không quan tâm hoa thường (Dùng cho tìm kiếm, đăng nhập)
     */
    public boolean isMatch(String target, String searchKeyword) {
        if (target == null || searchKeyword == null) return false;
        return target.trim().equalsIgnoreCase(searchKeyword.trim());
    }

    // --- GETTERS & SETTERS ---
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }

    public void setPassword(String password) { this.password = password; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}