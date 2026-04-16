# TutorFinder_Online - Hệ thống Kết nối Gia sư online

Là một ứng dụng Java Console được thiết kế để kết nối Phụ huynh tìm kiếm gia sư tại nhà và Gia sư có nhu cầu nhận lớp

## Tính năng chính

### 1. Phụ huynh (Parent)
* **Tìm kiếm Gia sư:** Lọc theo môn học, lớp và khu vực.
* **Xem hồ sơ chi tiết:** Xem thông tin gia sư và số điện thoại để liên hệ.
* **Đăng bài tìm gia sư:** Tạo yêu cầu tìm gia sư .
* **Quản lý bài đăng:** Xem danh sách gia sư đăng ký nhận lớp và có quyền xóa bài đăng khi đã tìm được người.
* **Đánh giá:** đánh giá sao cho gia sư sau khi trải nghiệm dịch vụ.
* **Bảo mật:** đổi mật khẩu

### 2. Gia sư (Tutor)
* **Đăng ký hồ sơ:** 
* **Tìm kiếm lớp học:** Theo dõi các bài đăng mới nhất từ phụ huynh đã được Admin duyệt.
* **Đăng ký/Hủy nhận lớp:** Đăng ký dạy các lớp phù hợp hoặc hủy đăng ký.
* **Xem thống kê đánh giá:** Theo dõi số sao trung bình và lượt đánh giá từ phụ huynh.
* **Sửa hồ sơ:** Thay đổi hồ sơ xong thì phải chờ admin duyệt
* **Bảo mật:** đổi mật khẩu

### 3. Quản trị viên (Admin)
* **Duyệt tài khoản:** Kiểm duyệt thông tin gia sư trước khi cho phép tham gia hệ thống.
* **Kiểm duyệt bài đăng:** Đảm bảo các yêu cầu tìm gia sư của phụ huynh là hợp lệ.
* **Quản lý người dùng:** Theo dõi danh sách toàn bộ tài khoản trên hệ thống
* **Hỗ trợ tài khoản:** Đặt lại mật khẩu tài khoản báo quên hoặc mất quyền truy cập.

## Công nghệ sử dụng
* **Ngôn ngữ:** Java (JDK 8+).
* **Kiến trúc:** Object-Oriented Programming (OOP) chia lớp Manager xử lý nghiệp vụ riêng biệt.
* **Lưu trữ dữ liệu:** `Serializable`: Ghi/Đọc file nhị phân (`.dat`) .
* **Cơ chế đồng bộ:** Tự động nạp dữ liệu (Load) liên tục giúp chạy song song nhiều cửa sổ Console cùng lúc.

## Cấu trúc dự án
```text
src/com/tutorfinder/
├── data/
│   └── Database.java          # Nhà kho lưu trữ và xử lý file
├── managers/
│   ├── AdminManager.java      # tính năng  Admin
│   ├── AuthManager.java       # Xử lý Đăng ký/Đăng nhập
│   ├── ParentManager.java     # tính năng Phụ huynh
│   └── TutorManager.java      # tính năng Gia sư
├── model/
│   ├── User.java (Abstract)   # Lớp cha
│   ├── Parent.java            # Model Phụ huynh
│   ├── Tutor.java             # Model Gia sư
│   ├── Admin.java             # Model Admin
│   └── JobPost.java           # Model Bài đăng tìm gia sư
└── Main.java                  # Khởi chạy chương trình
```

##  Hướng dẫn chạy chương trình
1. Mở dự án bằng bất kỳ IDE nào (IntelliJ, Eclipse, NetBeans).
2. Chạy file `Main.java`.
3. **Tài khoản Admin mặc định:** * Username: `admin`
    * Password: `admin`
4. Dữ liệu sẽ được lưu tại file `data.dat`.

##  Lưu ý vận hành
* Khi đăng ký, hệ thống sẽ tự động sinh ID (ví dụ: `p1`, `t1`, `jp1`).
* Gia sư và bài đăng mới cần phải được **Admin duyệt** thì mới có thể nhìn thấy nhau trên hệ thống.
* Tại bất kỳ bước nhập liệu nào, người dùng có thể nhấn **0** hoặc **Enter trống** để quay lại menu trước đó.

