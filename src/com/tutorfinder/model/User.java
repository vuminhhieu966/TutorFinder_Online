package com.tutorfinder.model;

import java.io.Serializable;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String username;
    protected String password;
    protected String phone;
    protected double walletBalance;

    public User(String id, String username, String password, String phone) {
        this.id = id; this.username = username;
        this.password = password; this.phone = phone;
        this.walletBalance = 0.0;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public double getWalletBalance() { return walletBalance; }

    public void addMoney(double amount) { this.walletBalance += amount; }
    public boolean withdrawMoney(double amount) {
        if (this.walletBalance >= amount) {
            this.walletBalance -= amount; return true;
        }
        return false;
    }
    public abstract String getRole();
}