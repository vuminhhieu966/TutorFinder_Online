package com.tutorfinder.model;

import java.io.Serializable;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String username;
    protected String password;
    protected String name;
    protected String phone;

    public User(String id, String username, String password, String name, String phone) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    // Getter
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getPhone() { return phone; }

    // Setter
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }

    // Hàm trừu tượng
    public abstract String getRole();
}