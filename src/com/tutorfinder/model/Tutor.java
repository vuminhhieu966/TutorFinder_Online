package com.tutorfinder.model;

public class Tutor extends User {
    private static final long serialVersionUID = 1L;
    private String subject;
    private double rating;
    private int reviewCount;
    private int status; // 0: Chờ duyệt, 1: Đã duyệt, -1: Bị khóa

    public Tutor(String id, String username, String password, String phone, String subject) {
        super(id, username, password, phone);
        this.subject = subject; this.rating = 0.0;
        this.reviewCount = 0; this.status = 0;
    }

    @Override
    public String getRole() { return "TUTOR"; }
    public String getSubject() { return subject; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public void addReview(double newRating) {
        double total = this.rating * this.reviewCount;
        this.reviewCount++;
        this.rating = (total + newRating) / this.reviewCount;
    }
}