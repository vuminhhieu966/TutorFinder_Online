package com.tutorfinder.model;

public class Admin extends User {
    private static final long serialVersionUID = 1L;

    public Admin(String id, String username, String password) {
        super(id, username, password, "N/A", "N/A");
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}