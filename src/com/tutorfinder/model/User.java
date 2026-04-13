package com.tutorfinder.model;

import java.io.Serializable;

/**
 * Lớp User (Khuôn mẫu cha)
 * - Đánh dấu implements Serializable để toàn bộ dữ liệu của User
 * (và các class con kế thừa nó) có thể được chuyển thành mã nhị phân và lưu xuống file .dat.
 */
public abstract class User implements Serializable {
    // serialVersionUID giúp Java nhận diện đúng phiên bản của class khi đọc/ghi file
    // Tránh lỗi InvalidClassException nếu sau này bạn thêm/bớt thuộc tính
    private static final long serialVersionUID = 1L;

    // Các thuộc tính cơ bản mà tài khoản nào cũng phải có
    private String id;          // Mã định danh (t1, p1, admin...)
    private String username;    // Tên đăng nhập
    private String password;    // Mật khẩu
    private String fullName;    // Họ và tên người dùng
    private String phone;       // Số điện thoại liên hệ
    private String role;        // Vai trò: "ADMIN", "PARENT", "TUTOR"

    /**
     * Constructor khởi tạo tài khoản mới
     */
    public User(String id, String username, String password, String fullName, String phone, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.role = role;
    }

    /**
     * Phương thức trừu tượng displayMenu()
     * - Bắt buộc các class con (Admin, Parent, Tutor) phải tự định nghĩa menu riêng của mình.
     */
    public abstract void displayMenu();

    /**
     * Hàm tiện ích dùng chung để kiểm tra xem người dùng có muốn thoát/quay lại không
     * @param input: Ký tự người dùng nhập vào từ bàn phím
     * @return true nếu nhập "0" hoặc "exit"
     */
    public boolean isExit(String input) {
        return input.trim().equals("0") || input.trim().equalsIgnoreCase("exit");
    }

    // ==========================================
    // GETTERS & SETTERS (Lấy và sửa dữ liệu)
    // ==========================================

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }
    /**
     * Hàm so sánh chuỗi thông minh:
     * - Không quan tâm hoa thường (equalsIgnoreCase)
     * - Tự động loại bỏ khoảng trắng thừa (trim)
     */
    public boolean isMatch(String s1, String s2) {
        if (s1 == null || s2 == null) return false;
        return s1.trim().equalsIgnoreCase(s2.trim());
    }
}