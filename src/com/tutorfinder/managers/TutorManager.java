package com.tutorfinder.managers;

import com.tutorfinder.data.DataStore;
import com.tutorfinder.model.*;
import java.util.Scanner;

public class TutorManager {
    private DataStore db = DataStore.getInstance();
    private Scanner sc;

    public TutorManager(Scanner sc) { this.sc = sc; }

    public void findJobPosts(Tutor t) {
        System.out.println("\n--- TÌM LỚP HỌC (BÀI ĐĂNG) ---");
        System.out.print("Lọc môn: "); String sub = sc.nextLine().trim();
        for (JobPost jp : db.jobPosts) {
            if (sub.isEmpty() || jp.getSubject().equalsIgnoreCase(sub)) {
                System.out.printf("Mã BĐ: %s | Môn: %s | Phí: %.0f\n", jp.getId(), jp.getSubject(), jp.getFeePerSession());
            }
        }
        System.out.print("Nhập Mã BĐ để đăng ký (Enter bỏ qua): "); String jpId = sc.nextLine().trim();
        for (JobPost jp : db.jobPosts) {
            if (jp.getId().equals(jpId) && !jp.getRegisteredTutorIds().contains(t.getId())) {
                jp.getRegisteredTutorIds().add(t.getId());
                DataStore.saveData();
                System.out.println("Đăng ký thành công! Chờ Phụ huynh chọn.");
            }
        }
    }

    public void manageRequests(Tutor t) {
        System.out.println("\n--- THƯ MỜI TỪ PHỤ HUYNH ---");
        for (Request r : db.requests) {
            if (r.getTutorId().equals(t.getId()) && r.getStatus() == 0) {
                System.out.printf("Mã YC: %s | Môn: %s | Phí: %.0f\n", r.getId(), r.getSubject(), r.getFeePerSession());
            }
        }
        System.out.print("Nhập Mã YC để Nhận (+ Mã) hoặc Từ chối (- Mã) [VD: r1 hoặc -r1]: "); String act = sc.nextLine().trim();
        if (!act.isEmpty()) {
            boolean reject = act.startsWith("-");
            String rId = reject ? act.substring(1) : act;
            for (Request r : db.requests) {
                if (r.getId().equals(rId) && r.getTutorId().equals(t.getId()) && r.getStatus() == 0) {
                    if (reject) { r.setStatus(-1); DataStore.saveData(); System.out.println("Đã từ chối!"); }
                    else {
                        double sysFee = r.getFeePerSession() * 2;
                        if (t.withdrawMoney(sysFee)) {
                            db.systemRevenue += sysFee;
                            r.setStatus(1);
                            Course c = new Course(db.genId("c"), r.getParentId(), t.getId(), r.getFeePerSession());
                            db.courses.add(c);
                            r.setCourseId(c.getId());
                            DataStore.saveData();
                            System.out.println("Nhận lớp thành công! Đã trừ " + sysFee + " phí hệ thống. Mã lớp: " + c.getId());
                        } else {
                            System.out.println("Ví không đủ trả phí 2 buổi để nhận lớp!");
                        }
                    }
                }
            }
        }
    }

    public void manageCourses(Tutor t) {
        System.out.println("\n--- CÁC LỚP HỌC CỦA TÔI ---");
        for (Course c : db.courses) {
            if (c.getTutorId().equals(t.getId())) {
                String st = c.getStatus() == 0 ? "CHỜ MỞ LỚP" : (c.getStatus() == 1 ? "ĐÃ MỞ LỚP" : (c.getStatus() == 2 ? "ĐANG DẠY" : "ĐÃ KẾT THÚC"));
                System.out.printf("Mã Lớp: %s | PH: %s | Trạng thái: %s\n", c.getId(), c.getParentId(), st);
            }
        }
        System.out.println("1. Mở lớp (Cho PH vào học) | 2. Đóng lớp (Kết thúc) | 0. Thoát");
        System.out.print("Chọn: "); String opt = sc.nextLine();

        if (opt.equals("1")) {
            System.out.print("Nhập Mã Lớp cần MỞ: "); String cId = sc.nextLine();
            for (Course c : db.courses) {
                if (c.getId().equals(cId) && c.getTutorId().equals(t.getId()) && c.getStatus() == 0) {
                    c.setStatus(1); // Mở lớp
                    DataStore.saveData();
                    System.out.println("MỞ LỚP THÀNH CÔNG! Phụ huynh đã có thể thanh toán để vào học.");
                }
            }
        } else if (opt.equals("2")) {
            System.out.print("Nhập Mã Lớp cần ĐÓNG: "); String cId = sc.nextLine();
            for (Course c : db.courses) {
                if (c.getId().equals(cId) && c.getTutorId().equals(t.getId()) && c.getStatus() == 2) {
                    c.setStatus(3); // Kết thúc
                    DataStore.saveData();
                    System.out.println("ĐÓNG LỚP THÀNH CÔNG! Buổi học kết thúc.");
                } else if (c.getId().equals(cId) && c.getStatus() < 2) {
                    System.out.println("Không thể đóng lớp khi Phụ huynh chưa vào học!");
                }
            }
        }
    }
}