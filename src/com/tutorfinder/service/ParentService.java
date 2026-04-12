package com.tutorfinder.service;

import com.tutorfinder.model.*;
import java.util.*;

public class ParentService {

    // 1. TÌM KIẾM GIA SƯ & GỬI YÊU CẦU TRỰC TIẾP
    public static void searchAndRequest(Scanner sc, Parent p) {
        DataService.loadData(); // Đồng bộ dữ liệu mới nhất
        System.out.println("\n--- TÌM KIẾM GIA SƯ (Nhấn Enter để bỏ qua tiêu chí) ---");

        System.out.print("Môn học: "); String sub = sc.nextLine().trim();
        System.out.print("Khu vực: "); String area = sc.nextLine().trim();
        System.out.print("Mức sao tối thiểu (0-5): ");
        String starInput = sc.nextLine().trim();
        double minStar = starInput.isEmpty() ? 0 : Double.parseDouble(starInput);

        List<Tutor> results = new ArrayList<>();
        for (User u : DataService.allUsers) {
            if (u instanceof Tutor t && t.isApproved()) {
                // Logic: Rỗng thì khớp, không rỗng thì so sánh không hoa thường
                boolean matchSub = sub.isEmpty() || p.isMatch(t.getSubject(), sub);
                boolean matchArea = area.isEmpty() || p.isMatch(t.getArea(), area);
                boolean matchStar = t.getRating() >= minStar;

                if (matchSub && matchArea && matchStar) {
                    results.add(t);
                }
            }
        }

        if (results.isEmpty()) {
            System.out.println("❌ Không tìm thấy gia sư phù hợp.");
            return;
        }

        System.out.println("\n--- KẾT QUẢ ---");
        for (Tutor t : results) {
            System.out.println("ID: " + t.getId() + " | Tên: " + t.getFullName() +
                    " | Môn: " + t.getSubject() + " | Sao: " + t.getRating() + " ⭐");
        }

        System.out.print("\n➤ Nhập ID Gia sư để gửi lời mời dạy (hoặc '0' để quay lại): ");
        String tutorId = sc.nextLine();
        if (p.isExit(tutorId)) return;

        for (Tutor t : results) {
            if (t.getId().equalsIgnoreCase(tutorId)) {
                // Thêm ID của Phụ huynh vào danh sách thư mời của Gia sư
                if (!t.getIncomingInvitations().contains(p.getId())) {
                    t.getIncomingInvitations().add(p.getId());
                    System.out.println("✅ Đã gửi lời mời tới gia sư " + t.getFullName());
                    DataService.saveData();
                } else {
                    System.out.println("⚠️ Bạn đã gửi yêu cầu cho gia sư này rồi.");
                }
                return;
            }
        }
    }

    // 2. ĐĂNG BÀI TÌM GIA SƯ
    public static void createPost(Scanner sc, String parentId) {
        System.out.println("\n--- ĐĂNG TIN TÌM GIA SƯ ---");
        System.out.print("Môn học: "); String sub = sc.nextLine();
        System.out.print("Khu vực: "); String area = sc.nextLine();
        System.out.print("Học phí đề xuất/buổi: "); double fee = Double.parseDouble(sc.nextLine());

        String postId = "P" + (DataService.activePosts.size() + 100);
        Post newPost = new Post(postId, parentId, sub, area, fee);
        DataService.activePosts.add(newPost);
        DataService.saveData();
        System.out.println("✅ Đã đăng tin! Mã tin: " + postId);
    }

    // 3. QUẢN LÝ BÀI ĐĂNG (Xem ứng viên & Chọn)
    public static void managePosts(Scanner sc, Parent p) {
        DataService.loadData();
        System.out.println("\n--- BÀI ĐĂNG CỦA BẠN ---");
        List<Post> myPosts = new ArrayList<>();
        for (Post post : DataService.activePosts) {
            if (post.getParentId().equals(p.getId())) {
                System.out.println("[" + post.getPostId() + "] " + post.getSubject() +
                        " | Trạng thái: " + post.getStatus() +
                        " | Ứng viên: " + post.getApplicantIds().size());
                myPosts.add(post);
            }
        }

        if (myPosts.isEmpty()) return;

        System.out.print("\n➤ Chọn mã bài đăng để xem ứng viên hoặc 'del [mã]' để xóa: ");
        String input = sc.nextLine();
        if (p.isExit(input)) return;

        if (input.startsWith("del ")) {
            String idDel = input.substring(4);
            DataService.activePosts.removeIf(post -> post.getPostId().equalsIgnoreCase(idDel));
            System.out.println("✅ Đã xóa bài đăng.");
            DataService.saveData();
            return;
        }

        for (Post post : myPosts) {
            if (post.getPostId().equalsIgnoreCase(input)) {
                System.out.println("Gia sư ứng tuyển: " + post.getApplicantIds());
                System.out.print("Nhập ID Gia sư bạn chọn: ");
                String tutorId = sc.nextLine();
                if (post.getApplicantIds().contains(tutorId)) {
                    post.setStatus("CLOSED");
                    DataService.allEnrollments.add(new Enrollment(post.getPostId(), p.getId(), tutorId));
                    System.out.println("✅ Đã chốt lớp thành công!");
                    DataService.saveData();
                }
                break;
            }
        }
    }

    // 4. LỚP ĐANG HỌC & KHIẾU NẠI
    public static void classManagement(Scanner sc, Parent p) {
        DataService.loadData();
        System.out.println("\n--- CÁC LỚP ĐANG HỌC ---");
        for (Enrollment e : DataService.allEnrollments) {
            if (e.getParentId().equals(p.getId())) {
                System.out.println("Lớp: " + e.getPostId() + " | Gia sư: " + e.getTutorId());
            }
        }
        System.out.print("\n1. Đánh giá | 2. Khiếu nại | 0. Quay lại: ");
        String opt = sc.nextLine();
        if (opt.equals("2")) {
            System.out.print("Nhập mã lớp khiếu nại: "); String pid = sc.nextLine();
            System.out.print("Nội dung: "); String content = sc.nextLine();
            DataService.allComplaints.add(new Complaint("C"+System.currentTimeMillis(), p.getId(), pid, content));
            DataService.saveData();
            System.out.println("✅ Đã gửi khiếu nại.");
        }
    }
}