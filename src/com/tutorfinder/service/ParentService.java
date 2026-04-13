package com.tutorfinder.service;

import com.tutorfinder.model.*;
import java.util.*;
import java.util.stream.Collectors;

public class ParentService {
    private static Scanner sc = new Scanner(System.in);

    // 1. TÌM GIA SƯ (Lọc theo Môn/Sao/Lượt đánh giá - Sắp xếp Sao giảm dần)
    public static void searchTutor(Parent p) {
        System.out.print("➤ Nhập môn học (Bỏ qua nhấn Enter): ");
        String sub = sc.nextLine().trim();
        System.out.print("➤ Nhập số sao tối thiểu (0-5, Bỏ qua nhấn Enter): ");
        String starInput = sc.nextLine().trim();
        double minStar = starInput.isEmpty() ? 0 : Double.parseDouble(starInput);

        // Lọc và Sắp xếp: Chỉ lấy Gia sư đã DUYỆT, khớp môn và đủ sao
        List<Tutor> tutors = DataService.allUsers.stream()
                .filter(u -> u instanceof Tutor)
                .map(u -> (Tutor) u)
                .filter(t -> t.getStatus().equals("APPROVED"))
                .filter(t -> sub.isEmpty() || t.isMatch(t.getSubject(), sub))
                .filter(t -> t.getRating() >= minStar)
                .sorted(Comparator.comparing(Tutor::getRating).reversed()) // SAO GIẢM DẦN
                .collect(Collectors.toList());

        System.out.println("\n--- DANH SÁCH GIA SƯ PHÙ HỢP ---");
        if (tutors.isEmpty()) {
            System.out.println("❌ Không tìm thấy gia sư nào!");
            return;
        }

        for (Tutor t : tutors) {
            System.out.printf("[%s] %-15s | Môn: %-10s | ⭐ %-3.1f (%d lượt)\n",
                    t.getId(), t.getFullName(), t.getSubject(), t.getRating(), t.getReviewCount());
        }

        System.out.print("\n➤ Nhập ID Gia sư để gửi yêu cầu (hoặc 0 để quay lại): ");
        String tutorId = sc.nextLine().trim();
        if (tutorId.equals("0")) return;

        // Gửi yêu cầu trực tiếp
        sendDirectRequest(p, tutorId);
    }

    private static void sendDirectRequest(Parent p, String tutorId) {
        System.out.print("Môn học cần học: "); String sub = sc.nextLine();
        System.out.print("Số tiền / buổi: "); double fee = Double.parseDouble(sc.nextLine());
        System.out.print("Số phút / buổi: "); int mins = Integer.parseInt(sc.nextLine());

        String reqId = DataService.generateId("r");
        DataService.allRequests.add(new Request(reqId, p.getId(), tutorId, sub, fee, mins));
        System.out.println("✅ Đã gửi lời mời đến gia sư " + tutorId);
    }

    // 2. ĐĂNG BÀI TÌM GIA SƯ
    public static void createPost(Parent p) {
        System.out.println("\n--- ĐĂNG TIN TÌM GIA SƯ ---");
        System.out.print("Môn học: "); String sub = sc.nextLine();
        System.out.print("Học phí dự kiến / buổi: "); double fee = Double.parseDouble(sc.nextLine());
        System.out.print("Thời lượng (phút) / buổi: "); int mins = Integer.parseInt(sc.nextLine());

        String id = DataService.generateId("po");
        DataService.allPosts.add(new Post(id, p.getId(), sub, fee, mins));
        System.out.println("✅ Bài đăng [" + id + "] đã được đưa lên hệ thống.");
    }

    // 3. VÍ CÁ NHÂN (Nạp/Rút)
    public static void manageWallet(Parent p) {
        System.out.println("\n--- VÍ CÁ NHÂN ---");
        System.out.println("💰 Số dư hiện tại: " + p.getBalance() + " VNĐ");
        System.out.println("1. Nạp tiền | 2. Rút tiền | 0. Quay lại");
        String opt = sc.nextLine();
        if (opt.equals("1")) {
            System.out.print("Nhập số tiền nạp: ");
            p.setBalance(p.getBalance() + Double.parseDouble(sc.nextLine()));
            System.out.println("✅ Nạp tiền thành công!");
        } else if (opt.equals("2")) {
            System.out.print("Nhập số tiền rút: ");
            double amt = Double.parseDouble(sc.nextLine());
            if (amt <= p.getBalance()) {
                p.setBalance(p.getBalance() - amt);
                System.out.println("✅ Rút tiền thành công!");
            } else System.out.println("❌ Số dư không đủ!");
        }
    }

    // 4. CÁC ĐƠN YÊU CẦU
    public static void manageRequests(Parent p) {
        System.out.println("\n--- ĐƠN YÊU CẦU ĐÃ GỬI ---");
        for (Request r : DataService.allRequests) {
            if (r.getParentId().equals(p.getId())) {
                System.out.printf("[%s] Gia sư: %s | Trạng thái: %s | %s\n",
                        r.getReqId(), r.getTutorId(), r.getStatus(),
                        (r.getStatus().equals("ACCEPTED") ? "Mã lớp: CLS_" + r.getReqId() : ""));
            }
        }
        System.out.print("➤ Nhập mã đơn để HỦY (hoặc 0): ");
        String id = sc.nextLine();
        DataService.allRequests.removeIf(r -> r.getReqId().equals(id) && r.getParentId().equals(p.getId()));
    }

    // 5. CÁC BÀI ĐĂNG (Chọn Gia sư ứng tuyển)
    public static void managePosts(Parent p) {
        System.out.println("\n--- BÀI ĐĂNG CỦA BẠN ---");
        for (Post po : DataService.allPosts) {
            if (po.getParentId().equals(p.getId())) {
                System.out.printf("[%s] Môn: %-10s | Ứng viên: %d | Trạng thái: %s\n",
                        po.getPostId(), po.getSubject(), po.getApplicantIds().size(), po.getStatus());
            }
        }
        System.out.print("➤ Chọn ID bài đăng để xem ứng viên: ");
        String poId = sc.nextLine();
        for (Post po : DataService.allPosts) {
            if (po.getPostId().equals(poId) && po.getStatus().equals("OPEN")) {
                System.out.println("Danh sách Gia sư ứng tuyển: " + po.getApplicantIds());
                System.out.print("➤ Nhập ID Gia sư bạn chọn: ");
                String tId = sc.nextLine();
                if (po.getApplicantIds().contains(tId)) {
                    // Chốt lớp -> Tạo ClassRoom trạng thái PENDING_FEE
                    String cId = DataService.generateId("c");
                    DataService.allClasses.add(new ClassRoom(cId, p.getId(), tId, po.getSubject(), po.getFee(), po.getMinutes()));
                    po.setStatus("CLOSED");
                    System.out.println("✅ Đã chốt gia sư. Chờ gia sư đóng phí để mở lớp.");
                }
            }
        }
    }

    // 6. CÁC LỚP HỌC (Vào học / Đánh giá / Khiếu nại)
    public static void manageClasses(Parent p) {
        System.out.println("\n--- LỚP HỌC CỦA BẠN ---");
        for (ClassRoom c : DataService.allClasses) {
            if (c.getParentId().equals(p.getId())) {
                System.out.printf("[%s] Môn: %-10s | GS: %-10s | Trạng thái: %s\n",
                        c.getClassId(), c.getSubject(), c.getTutorId(), c.getStatus());
            }
        }
        System.out.print("➤ Nhập mã lớp: ");
        String cid = sc.nextLine();
        System.out.println("1. Vào học (Thanh toán 1 buổi) | 2. Đánh giá | 3. Khiếu nại | 0. Quay lại");
        String opt = sc.nextLine();

        for (ClassRoom c : DataService.allClasses) {
            if (c.getClassId().equals(cid)) {
                if (opt.equals("1")) {
                    handleJoinClass(p, c);
                } else if (opt.equals("2")) {
                    handleRating(c);
                } else if (opt.equals("3")) {
                    handleComplaint(p, c);
                }
            }
        }
    }

    private static void handleJoinClass(Parent p, ClassRoom c) {
        if (!c.getStatus().equals("OPEN")) {
            System.out.println("❌ Lớp chưa mở hoặc Gia sư chưa kích hoạt!");
            return;
        }
        if (p.getBalance() < c.getFee()) {
            System.out.println("❌ Ví không đủ tiền thanh toán buổi học này (" + c.getFee() + "đ)!");
            return;
        }
        p.setBalance(p.getBalance() - c.getFee());
        DataService.totalRevenue += c.getFee() * 0.1; // Hệ thống thu phí 10%
        System.out.println("✅ Đã thanh toán " + c.getFee() + "đ. Đang kết nối vào phòng học Online...");
        System.out.println("🎬 [VIDEO CALL STARTING...] Bạn đang học môn " + c.getSubject());
    }

    private static void handleRating(ClassRoom c) {
        System.out.print("Nhập số sao (1-5): ");
        double stars = Double.parseDouble(sc.nextLine());
        for (User u : DataService.allUsers) {
            if (u.getId().equals(c.getTutorId()) && u instanceof Tutor t) {
                // Công thức tính trung bình sao
                double totalStars = t.getRating() * t.getReviewCount() + stars;
                t.setReviewCount(t.getReviewCount() + 1);
                t.setRating(totalStars / t.getReviewCount());
                System.out.println("✅ Cảm ơn bạn đã đánh giá!");
            }
        }
    }

    private static void handleComplaint(Parent p, ClassRoom c) {
        System.out.print("Nội dung khiếu nại: ");
        String content = sc.nextLine();
        String cpId = DataService.generateId("cp");
        DataService.allComplaints.add(new Complaint(cpId, p.getId(), c.getClassId(), content));
        System.out.println("✅ Đã gửi đơn khiếu nại mã [" + cpId + "]. Admin sẽ xử lý sớm.");
    }
}