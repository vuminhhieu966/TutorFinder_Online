package com.tutorfinder.service;

import com.tutorfinder.model.*;
import java.util.Scanner;

public class AdminService {
    public static void approveTutors(Scanner sc) {
        System.out.println("\n--- GIA SƯ CHỜ DUYỆT ---");
        for (User u : DataService.allUsers) {
            if (u instanceof Tutor && !((Tutor) u).isApproved()) {
                System.out.println("ID: " + u.getId() + " | Tên: " + u.getFullName() + " | Môn: " + ((Tutor) u).getSubject());
            }
        }
        System.out.print("Nhập ID gia sư để duyệt (0 để thoát): ");
        String id = sc.nextLine();
        if (id.equals("0")) return;

        for (User u : DataService.allUsers) {
            if (u.getId().equals(id) && u instanceof Tutor) {
                ((Tutor) u).setApproved(true);
                System.out.println("✅ Đã duyệt cho: " + u.getFullName());
                return;
            }
        }
        System.out.println("❌ Không tìm thấy ID gia sư này.");
    }
}