package com.tutorfinder.model;

public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin(String id, String username, String password) {
        // Admin mặc định tên là "Hệ thống" và không cần số điện thoại
        super(id, username, password, "Hệ thống", "N/A");
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}