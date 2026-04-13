package com.tutorfinder.model;

public class Parent extends User {
    private static final long serialVersionUID = 1L;
    public Parent(String id, String username, String password, String phone) {
        super(id, username, password, phone);
    }
    @Override
    public String getRole() { return "PARENT"; }
}