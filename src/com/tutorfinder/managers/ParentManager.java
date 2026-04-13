package com.tutorfinder.managers;

import com.tutorfinder.data.DataStore;
import com.tutorfinder.model.*;
import java.util.Scanner;

public class ParentManager {
    private DataStore db = DataStore.getInstance();
    private Scanner sc;

    public ParentManager(Scanner sc) { this.sc = sc; }

    public void findTutors(Parent p) {
        System.out.println("\n--- TÌM KIẾM GIA SƯ ---");
        System.out.print("Lọc theo môn (Enter để xem tất cả): "); String sub = sc.nextLine().trim();

        db.users.stream()
                .filter(u -> u instanceof Tutor && ((Tutor) u).getStatus() == 1)
                .map(u -> (Tutor) u)
                .filter(t -> sub.isEmpty() || t.getSubject().equalsIgnoreCase(sub))
                .sorted((t1, t2) -> Double.compare(t2.getRating(), t1.getRating())) // Xếp sao giảm dần
                .forEach(t -> System.out.printf("ID: %s | Tên: %s | Môn: %s | Sao: %.1f | Lượt ĐG: %d\n",
                        t.getId(), t.getUsername(), t.getSubject(), t.getRating(), t.getReviewCount()));

        System.out.print("\nNhập ID Gia sư để gửi Yêu cầu (Enter bỏ qua): "); String tId = sc.nextLine().trim();
        if (!tId.isEmpty() && db.getUserById(tId) != null) {
            System.out.print("Môn học: "); String m = sc.nextLine();
            System.out.print("Số tiền/buổi: "); double fee = Double.parseDouble(sc.nextLine());
            System.out.print("Số phút/buổi: "); int min = Integer.parseInt(sc.nextLine());

            db.requests.add(new Request(db.genId("r"), p.getId(), tId, m, fee, min));
            DataStore.saveData();
            System.out.println("Gửi yêu cầu thành công!");
        }
    }

    public void createJobPost(Parent p) {
        System.out.println("\n--- ĐĂNG BÀI TÌM GIA SƯ ---");
        System.out.print("Môn học: "); String m = sc.nextLine();
        System.out.print("Số tiền/buổi: "); double fee = Double.parseDouble(sc.nextLine());
        System.out.print("Số phút/buổi: "); int min = Integer.parseInt(sc.nextLine());

        db.jobPosts.add(new JobPost(db.genId("jp"), p.getId(), m, fee, min));
        DataStore.saveData();
        System.out.println("Đăng bài thành công!");
    }

    public void manageRequests(Parent p) {
        System.out.println("\n--- CÁC ĐƠN YÊU CẦU ĐÃ GỬI ---");
        for (Request r : db.requests) {
            if (r.getParentId().equals(p.getId())) {
                String st = r.getStatus() == 0 ? "Chờ GS nhận" : (r.getStatus() == 1 ? "Đã nhận (Mã lớp: "+r.getCourseId()+")" : "Từ chối/Hủy");
                System.out.printf("Mã YC: %s | ID GS: %s | Trạng thái: %s\n", r.getId(), r.getTutorId(), st);
            }
        }
        System.out.print("Nhập Mã YC muốn hủy (Enter bỏ qua): "); String rId = sc.nextLine().trim();
        for (Request r : db.requests) {
            if (r.getId().equals(rId) && r.getParentId().equals(p.getId()) && r.getStatus() == 0) {
                r.setStatus(-1);
                DataStore.saveData();
                System.out.println("Đã hủy yêu cầu!");
                return;
            }
        }
    }

    public void manageJobPosts(Parent p) {
        System.out.println("\n--- CÁC BÀI ĐĂNG CỦA TÔI ---");
        for (JobPost jp : db.jobPosts) {
            if (jp.getParentId().equals(p.getId())) {
                System.out.printf("Mã BĐ: %s | Môn: %s | Số GS đăng ký: %d\n", jp.getId(), jp.getSubject(), jp.getRegisteredTutorIds().size());
            }
        }
        System.out.print("Nhập Mã BĐ để chọn Gia sư (Enter bỏ qua): "); String jpId = sc.nextLine().trim();
        for (JobPost jp : db.jobPosts) {
            if (jp.getId().equals(jpId) && jp.getParentId().equals(p.getId())) {
                System.out.println("Danh sách Gia sư đã đăng ký:");
                for (String tId : jp.getRegisteredTutorIds()) {
                    Tutor t = (Tutor) db.getUserById(tId);
                    System.out.printf("- ID: %s | Tên: %s | SĐT: %s | Sao: %.1f\n", t.getId(), t.getUsername(), t.getPhone(), t.getRating());
                }
                System.out.print("Nhập ID Gia sư bạn muốn chọn: "); String cTid = sc.nextLine().trim();
                if (jp.getRegisteredTutorIds().contains(cTid)) {
                    // Tạo lớp học, trạng thái 0 (Chờ GS mở)
                    Course c = new Course(db.genId("c"), p.getId(), cTid, jp.getFeePerSession());
                    db.courses.add(c);
                    DataStore.saveData();
                    System.out.println("Chọn thành công! Đã tạo lớp " + c.getId() + ". Vui lòng chờ GS mở lớp.");
                }
            }
        }
    }

    public void manageCourses(Parent p) {
        System.out.println("\n--- CÁC LỚP HỌC ---");
        for (Course c : db.courses) {
            if (c.getParentId().equals(p.getId())) {
                Tutor t = (Tutor) db.getUserById(c.getTutorId());
                String st = c.getStatus() == 0 ? "CHỜ GS MỞ LỚP" : (c.getStatus() == 1 ? "ĐÃ MỞ - CHỜ THANH TOÁN" : (c.getStatus() == 2 ? "ĐANG HỌC" : "ĐÃ KẾT THÚC"));
                System.out.printf("Mã Lớp: %s | GS: %s | SĐT: %s | Phí: %.0f | TT: %s\n", c.getId(), t.getUsername(), t.getPhone(), c.getFeePerSession(), st);
            }
        }
        System.out.println("1. Vào học (Thanh toán) | 2. Đánh giá | 3. Khiếu nại | 0. Thoát");
        System.out.print("Chọn: "); String opt = sc.nextLine();

        if (opt.equals("1")) {
            System.out.print("Nhập Mã Lớp muốn vào: "); String cId = sc.nextLine();
            for (Course c : db.courses) {
                if (c.getId().equals(cId) && c.getParentId().equals(p.getId())) {
                    if (c.getStatus() == 0) { System.out.println("Lớp chưa được Gia sư mở!"); return; }
                    if (c.getStatus() >= 2) { System.out.println("Bạn đã ở trong lớp này hoặc lớp đã kết thúc!"); return; }

                    if (p.withdrawMoney(c.getFeePerSession())) {
                        Tutor t = (Tutor) db.getUserById(c.getTutorId());
                        t.addMoney(c.getFeePerSession()); // Chuyển tiền cho GS
                        c.setStatus(2); // Trạng thái Đang học
                        DataStore.saveData();
                        System.out.println("Thanh toán thành công! Bạn đã VÀO HỌC.");
                    } else {
                        System.out.println("Số dư ví không đủ, vui lòng nạp thêm!");
                    }
                }
            }
        } else if (opt.equals("2")) {
            System.out.print("Nhập ID Gia sư: "); String tId = sc.nextLine();
            System.out.print("Chấm sao (1-5): "); double star = Double.parseDouble(sc.nextLine());
            Tutor t = (Tutor) db.getUserById(tId);
            if (t != null) { t.addReview(star); DataStore.saveData(); System.out.println("Đánh giá thành công!"); }
        } else if (opt.equals("3")) {
            System.out.print("Nhập Mã Lớp cần khiếu nại: "); String cId = sc.nextLine();
            System.out.print("Lý do khiếu nại: "); String reason = sc.nextLine();
            System.out.print("Số tiền yêu cầu hoàn: "); double amount = Double.parseDouble(sc.nextLine());
            Course c = null; for(Course co : db.courses) if(co.getId().equals(cId)) c = co;
            if (c != null) {
                db.complaints.add(new Complaint(db.genId("comp"), p.getId(), c.getTutorId(), c.getId(), reason, amount));
                DataStore.saveData();
                System.out.println("Đã gửi đơn khiếu nại cho Admin xử lý!");
            }
        }
    }
}