package com.tutorfinder.managers;

import com.tutorfinder.data.DataStore;
import com.tutorfinder.model.*;
import java.util.Scanner;

public class AdminManager {
    private DataStore db = DataStore.getInstance();
    private Scanner sc;

    public AdminManager(Scanner sc) { this.sc = sc; }

    public void manageTutors() {
        System.out.println("\n--- QUẢN LÝ GIA SƯ ---");
        System.out.print("Lọc (1: Duyệt, 0: Chờ, -1: Khóa, Enter để xem tất cả): "); String filter = sc.nextLine().trim();
        for (User u : db.users) {
            if (u instanceof Tutor) {
                Tutor t = (Tutor) u;
                if (filter.isEmpty() || String.valueOf(t.getStatus()).equals(filter)) {
                    System.out.printf("ID: %s | Tên: %s | Trạng thái: %d\n", t.getId(), t.getUsername(), t.getStatus());
                }
            }
        }
        System.out.print("Nhập ID Gia sư cập nhật: "); String tId = sc.nextLine().trim();
        System.out.print("Trạng thái mới (1=Duyệt, 0=Chờ, -1=Khóa): "); String newSt = sc.nextLine().trim();
        Tutor t = (Tutor) db.getUserById(tId);
        if (t != null && !newSt.isEmpty()) {
            t.setStatus(Integer.parseInt(newSt));
            DataStore.saveData();
            System.out.println("Cập nhật thành công!");
        }
    }

    public void manageComplaints() {
        System.out.println("\n--- XỬ LÝ KHIẾU NẠI ---");
        for (Complaint cp : db.complaints) {
            if (cp.getStatus() == 0) {
                System.out.printf("Mã KN: %s | PH: %s | GS: %s | Lý do: %s | Đòi: %.0f\n",
                        cp.getId(), cp.getParentId(), cp.getTutorId(), cp.getReason(), cp.getAmountToRefund());
            }
        }
        System.out.print("Nhập Mã KN xử lý: "); String cpId = sc.nextLine();
        System.out.println("1. Bác bỏ (GS không sai) | 2. Chấp nhận (Trừ tiền GS -> Hoàn PH)");
        System.out.print("Chọn: "); String act = sc.nextLine();

        for (Complaint cp : db.complaints) {
            if (cp.getId().equals(cpId) && cp.getStatus() == 0) {
                if (act.equals("1")) {
                    cp.setStatus(-1); DataStore.saveData(); System.out.println("Đã bác bỏ!");
                } else if (act.equals("2")) {
                    Tutor t = (Tutor) db.getUserById(cp.getTutorId());
                    Parent p = (Parent) db.getUserById(cp.getParentId());
                    t.withdrawMoney(cp.getAmountToRefund()); // Ép trừ tiền GS
                    p.addMoney(cp.getAmountToRefund()); // Cộng tiền PH
                    cp.setStatus(1);
                    DataStore.saveData();
                    System.out.println("Đã hoàn tiền cho Phụ huynh!");
                }
            }
        }
    }

    public void showRevenue() {
        System.out.println("\n--- DOANH THU HỆ THỐNG ---");
        System.out.println("Tổng doanh thu: " + db.systemRevenue + " VNĐ");
    }
}