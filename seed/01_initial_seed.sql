-- ========================================================
-- StudyDocs Database Seed Data Script
-- Target Database: MySQL 8.0+
-- ========================================================

CREATE DATABASE IF NOT EXISTS `studydocs` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `studydocs`;

-- 1. Roles Seed
INSERT INTO `roles` (`name`, `description`) VALUES
('ADMIN', 'Administrator role with full access'),
('USER', 'Standard user role')
ON DUPLICATE KEY UPDATE `description` = VALUES(`description`);

-- 2. Users Seed
-- Passwords are BCrypt hashes ('admin123' and 'user123')
INSERT INTO `users` (
    `id`, `email`, `password`, `full_name`, `username`, `phone_number`, 
    `avatar_url`, `bio`, `gender`, `date_of_birth`, `address`, 
    `university_id`, `university_name`, `faculty_id`, `major`, `is_private`, `created_at`, `updated_at`
) VALUES
('usr-admin-001', 'admin@studydocs.com', '$2a$10$7R.xI/YyX28rA2lE6z.xOOd81wD7j1mR98mD7j1mR98mD7j1mR98m', 'StudyDocs Administrator', 'admin', '0987654321', 'https://i.pravatar.cc/150?u=admin', 'Hệ thống Quản trị viên StudyDocs', 'Male', '2000-01-01 00:00:00', '268 Lý Thường Kiệt, Q.10, TP.HCM', 1, 'Trường Đại học Bách Khoa - ĐHQG TP.HCM', 1, 'Khoa học Máy tính', 0, NOW(), NOW()),
('usr-user-002', 'hien@studydocs.com', '$2a$10$7R.xI/YyX28rA2lE6z.xOOd81wD7j1mR98mD7j1mR98mD7j1mR98m', 'Minh Hiển', 'minhhien', '0912345678', 'https://i.pravatar.cc/150?u=hien', 'Sinh viên Kỹ thuật Phần mềm', 'Male', '2002-05-15 00:00:00', 'TP. Thủ Đức, TP.HCM', 2, 'Trường Đại học Công nghệ Thông tin - ĐHQG TP.HCM', 2, 'Công nghệ Phần mềm', 0, NOW(), NOW())
ON DUPLICATE KEY UPDATE `full_name` = VALUES(`full_name`);

-- 3. User Roles Mapping Seed
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
('usr-admin-001', 'ADMIN'),
('usr-admin-001', 'USER'),
('usr-user-002', 'USER')
ON DUPLICATE KEY UPDATE `user_id` = `user_id`;

-- 4. Universities Seed
INSERT INTO `universities` (`id`, `name`, `code`, `logo_url`, `address`, `created_at`, `updated_at`) VALUES
(1, 'Trường Đại học Bách Khoa - ĐHQG TP.HCM', 'HCMUT', 'https://example.com/logo-hcmut.png', '268 Lý Thường Kiệt, Q.10, TP.HCM', NOW(), NOW()),
(2, 'Trường Đại học Công nghệ Thông tin - ĐHQG TP.HCM', 'UIT', 'https://example.com/logo-uit.png', 'Khu phố 6, P. Linh Trung, TP. Thủ Đức', NOW(), NOW()),
(3, 'Trường Đại học Khoa học Tự nhiên - ĐHQG TP.HCM', 'HCMUS', 'https://example.com/logo-hcmus.png', '227 Nguyễn Văn Cừ, Q.5, TP.HCM', NOW(), NOW())
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 5. Faculties Seed
INSERT INTO `faculties` (`id`, `name`, `university_id`, `created_at`, `updated_at`) VALUES
(1, 'Khoa Khoa học và Kỹ thuật Máy tính', 1, NOW(), NOW()),
(2, 'Khoa Điện - Điện tử', 1, NOW(), NOW()),
(3, 'Khoa Quản lý Công nghiệp', 1, NOW(), NOW()),
(4, 'Khoa Công nghệ Phần mềm', 2, NOW(), NOW()),
(5, 'Khoa Hệ thống Thông tin', 2, NOW(), NOW()),
(6, 'Khoa Mạng Máy tính và Truyền thông', 2, NOW(), NOW()),
(7, 'Khoa Toán - Tin học', 3, NOW(), NOW()),
(8, 'Khoa Vật lý - Vật lý Kỹ thuật', 3, NOW(), NOW()),
(9, 'Khoa Hóa học', 3, NOW(), NOW())
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 6. Departments Seed
INSERT INTO `departments` (`id`, `name`, `faculty_id`, `created_at`, `updated_at`) VALUES
(1, 'Bộ môn Khoa Khoa học và Kỹ thuật Máy tính 1', 1, NOW(), NOW()),
(2, 'Bộ môn Khoa Khoa học và Kỹ thuật Máy tính 2', 1, NOW(), NOW()),
(3, 'Bộ môn Khoa Khoa học và Kỹ thuật Máy tính 3', 1, NOW(), NOW()),
(4, 'Bộ môn Khoa Điện - Điện tử 1', 2, NOW(), NOW()),
(5, 'Bộ môn Khoa Điện - Điện tử 2', 2, NOW(), NOW()),
(6, 'Bộ môn Khoa Điện - Điện tử 3', 2, NOW(), NOW()),
(7, 'Bộ môn Khoa Quản lý Công nghiệp 1', 3, NOW(), NOW()),
(8, 'Bộ môn Khoa Quản lý Công nghiệp 2', 3, NOW(), NOW()),
(9, 'Bộ môn Khoa Quản lý Công nghiệp 3', 3, NOW(), NOW()),
(10, 'Bộ môn Khoa Công nghệ Phần mềm 1', 4, NOW(), NOW()),
(11, 'Bộ môn Khoa Công nghệ Phần mềm 2', 4, NOW(), NOW()),
(12, 'Bộ môn Khoa Công nghệ Phần mềm 3', 4, NOW(), NOW()),
(13, 'Bộ môn Khoa Hệ thống Thông tin 1', 5, NOW(), NOW()),
(14, 'Bộ môn Khoa Hệ thống Thông tin 2', 5, NOW(), NOW()),
(15, 'Bộ môn Khoa Hệ thống Thông tin 3', 5, NOW(), NOW()),
(16, 'Bộ môn Khoa Mạng Máy tính và Truyền thông 1', 6, NOW(), NOW()),
(17, 'Bộ môn Khoa Mạng Máy tính và Truyền thông 2', 6, NOW(), NOW()),
(18, 'Bộ môn Khoa Mạng Máy tính và Truyền thông 3', 6, NOW(), NOW()),
(19, 'Bộ môn Khoa Toán - Tin học 1', 7, NOW(), NOW()),
(20, 'Bộ môn Khoa Toán - Tin học 2', 7, NOW(), NOW()),
(21, 'Bộ môn Khoa Toán - Tin học 3', 7, NOW(), NOW()),
(22, 'Bộ môn Khoa Vật lý - Vật lý Kỹ thuật 1', 8, NOW(), NOW()),
(23, 'Bộ môn Khoa Vật lý - Vật lý Kỹ thuật 2', 8, NOW(), NOW()),
(24, 'Bộ môn Khoa Vật lý - Vật lý Kỹ thuật 3', 8, NOW(), NOW()),
(25, 'Bộ môn Khoa Hóa học 1', 9, NOW(), NOW()),
(26, 'Bộ môn Khoa Hóa học 2', 9, NOW(), NOW()),
(27, 'Bộ môn Khoa Hóa học 3', 9, NOW(), NOW())
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 7. Subjects Seed
INSERT INTO `subjects` (`id`, `name`, `code`, `department_id`, `created_at`, `updated_at`) VALUES
(1, 'Môn học Bộ môn Khoa Khoa học và Kỹ thuật Máy tính 1 1', 'SUBJ1', 1, NOW(), NOW()),
(2, 'Môn học Bộ môn Khoa Khoa học và Kỹ thuật Máy tính 1 2', 'SUBJ2', 1, NOW(), NOW()),
(3, 'Môn học Bộ môn Khoa Khoa học và Kỹ thuật Máy tính 1 3', 'SUBJ3', 1, NOW(), NOW()),
(4, 'Môn học Bộ môn Khoa Khoa học và Kỹ thuật Máy tính 2 1', 'SUBJ4', 2, NOW(), NOW()),
(5, 'Môn học Bộ môn Khoa Khoa học và Kỹ thuật Máy tính 2 2', 'SUBJ5', 2, NOW(), NOW()),
(6, 'Môn học Bộ môn Khoa Khoa học và Kỹ thuật Máy tính 2 3', 'SUBJ6', 2, NOW(), NOW()),
(7, 'Môn học Bộ môn Khoa Khoa học và Kỹ thuật Máy tính 3 1', 'SUBJ7', 3, NOW(), NOW()),
(8, 'Môn học Bộ môn Khoa Khoa học và Kỹ thuật Máy tính 3 2', 'SUBJ8', 3, NOW(), NOW()),
(9, 'Môn học Bộ môn Khoa Khoa học và Kỹ thuật Máy tính 3 3', 'SUBJ9', 3, NOW(), NOW()),
(10, 'Môn học Bộ môn Khoa Điện - Điện tử 1 1', 'SUBJ10', 4, NOW(), NOW()),
(11, 'Môn học Bộ môn Khoa Điện - Điện tử 1 2', 'SUBJ11', 4, NOW(), NOW()),
(12, 'Môn học Bộ môn Khoa Điện - Điện tử 1 3', 'SUBJ12', 4, NOW(), NOW()),
(13, 'Môn học Bộ môn Khoa Điện - Điện tử 2 1', 'SUBJ13', 5, NOW(), NOW()),
(14, 'Môn học Bộ môn Khoa Điện - Điện tử 2 2', 'SUBJ14', 5, NOW(), NOW()),
(15, 'Môn học Bộ môn Khoa Điện - Điện tử 2 3', 'SUBJ15', 5, NOW(), NOW()),
(16, 'Môn học Bộ môn Khoa Điện - Điện tử 3 1', 'SUBJ16', 6, NOW(), NOW()),
(17, 'Môn học Bộ môn Khoa Điện - Điện tử 3 2', 'SUBJ17', 6, NOW(), NOW()),
(18, 'Môn học Bộ môn Khoa Điện - Điện tử 3 3', 'SUBJ18', 6, NOW(), NOW()),
(19, 'Môn học Bộ môn Khoa Quản lý Công nghiệp 1 1', 'SUBJ19', 7, NOW(), NOW()),
(20, 'Môn học Bộ môn Khoa Quản lý Công nghiệp 1 2', 'SUBJ20', 7, NOW(), NOW()),
(21, 'Môn học Bộ môn Khoa Quản lý Công nghiệp 1 3', 'SUBJ21', 7, NOW(), NOW()),
(22, 'Môn học Bộ môn Khoa Quản lý Công nghiệp 2 1', 'SUBJ22', 8, NOW(), NOW()),
(23, 'Môn học Bộ môn Khoa Quản lý Công nghiệp 2 2', 'SUBJ23', 8, NOW(), NOW()),
(24, 'Môn học Bộ môn Khoa Quản lý Công nghiệp 2 3', 'SUBJ24', 8, NOW(), NOW()),
(25, 'Môn học Bộ môn Khoa Quản lý Công nghiệp 3 1', 'SUBJ25', 9, NOW(), NOW()),
(26, 'Môn học Bộ môn Khoa Quản lý Công nghiệp 3 2', 'SUBJ26', 9, NOW(), NOW()),
(27, 'Môn học Bộ môn Khoa Quản lý Công nghiệp 3 3', 'SUBJ27', 9, NOW(), NOW()),
(28, 'Môn học Bộ môn Khoa Công nghệ Phần mềm 1 1', 'SUBJ28', 10, NOW(), NOW()),
(29, 'Môn học Bộ môn Khoa Công nghệ Phần mềm 1 2', 'SUBJ29', 10, NOW(), NOW()),
(30, 'Môn học Bộ môn Khoa Công nghệ Phần mềm 1 3', 'SUBJ30', 10, NOW(), NOW()),
(31, 'Môn học Bộ môn Khoa Công nghệ Phần mềm 2 1', 'SUBJ31', 11, NOW(), NOW()),
(32, 'Môn học Bộ môn Khoa Công nghệ Phần mềm 2 2', 'SUBJ32', 11, NOW(), NOW()),
(33, 'Môn học Bộ môn Khoa Công nghệ Phần mềm 2 3', 'SUBJ33', 11, NOW(), NOW()),
(34, 'Môn học Bộ môn Khoa Công nghệ Phần mềm 3 1', 'SUBJ34', 12, NOW(), NOW()),
(35, 'Môn học Bộ môn Khoa Công nghệ Phần mềm 3 2', 'SUBJ35', 12, NOW(), NOW()),
(36, 'Môn học Bộ môn Khoa Công nghệ Phần mềm 3 3', 'SUBJ36', 12, NOW(), NOW()),
(37, 'Môn học Bộ môn Khoa Hệ thống Thông tin 1 1', 'SUBJ37', 13, NOW(), NOW()),
(38, 'Môn học Bộ môn Khoa Hệ thống Thông tin 1 2', 'SUBJ38', 13, NOW(), NOW()),
(39, 'Môn học Bộ môn Khoa Hệ thống Thông tin 1 3', 'SUBJ39', 13, NOW(), NOW()),
(40, 'Môn học Bộ môn Khoa Hệ thống Thông tin 2 1', 'SUBJ40', 14, NOW(), NOW()),
(41, 'Môn học Bộ môn Khoa Hệ thống Thông tin 2 2', 'SUBJ41', 14, NOW(), NOW()),
(42, 'Môn học Bộ môn Khoa Hệ thống Thông tin 2 3', 'SUBJ42', 14, NOW(), NOW()),
(43, 'Môn học Bộ môn Khoa Hệ thống Thông tin 3 1', 'SUBJ43', 15, NOW(), NOW()),
(44, 'Môn học Bộ môn Khoa Hệ thống Thông tin 3 2', 'SUBJ44', 15, NOW(), NOW()),
(45, 'Môn học Bộ môn Khoa Hệ thống Thông tin 3 3', 'SUBJ45', 15, NOW(), NOW()),
(46, 'Môn học Bộ môn Khoa Mạng Máy tính và Truyền thông 1 1', 'SUBJ46', 16, NOW(), NOW()),
(47, 'Môn học Bộ môn Khoa Mạng Máy tính và Truyền thông 1 2', 'SUBJ47', 16, NOW(), NOW()),
(48, 'Môn học Bộ môn Khoa Mạng Máy tính và Truyền thông 1 3', 'SUBJ48', 16, NOW(), NOW()),
(49, 'Môn học Bộ môn Khoa Mạng Máy tính và Truyền thông 2 1', 'SUBJ49', 17, NOW(), NOW()),
(50, 'Môn học Bộ môn Khoa Mạng Máy tính và Truyền thông 2 2', 'SUBJ50', 17, NOW(), NOW()),
(51, 'Môn học Bộ môn Khoa Mạng Máy tính và Truyền thông 2 3', 'SUBJ51', 17, NOW(), NOW()),
(52, 'Môn học Bộ môn Khoa Mạng Máy tính và Truyền thông 3 1', 'SUBJ52', 18, NOW(), NOW()),
(53, 'Môn học Bộ môn Khoa Mạng Máy tính và Truyền thông 3 2', 'SUBJ53', 18, NOW(), NOW()),
(54, 'Môn học Bộ môn Khoa Mạng Máy tính và Truyền thông 3 3', 'SUBJ54', 18, NOW(), NOW()),
(55, 'Môn học Bộ môn Khoa Toán - Tin học 1 1', 'SUBJ55', 19, NOW(), NOW()),
(56, 'Môn học Bộ môn Khoa Toán - Tin học 1 2', 'SUBJ56', 19, NOW(), NOW()),
(57, 'Môn học Bộ môn Khoa Toán - Tin học 1 3', 'SUBJ57', 19, NOW(), NOW()),
(58, 'Môn học Bộ môn Khoa Toán - Tin học 2 1', 'SUBJ58', 20, NOW(), NOW()),
(59, 'Môn học Bộ môn Khoa Toán - Tin học 2 2', 'SUBJ59', 20, NOW(), NOW()),
(60, 'Môn học Bộ môn Khoa Toán - Tin học 2 3', 'SUBJ60', 20, NOW(), NOW()),
(61, 'Môn học Bộ môn Khoa Toán - Tin học 3 1', 'SUBJ61', 21, NOW(), NOW()),
(62, 'Môn học Bộ môn Khoa Toán - Tin học 3 2', 'SUBJ62', 21, NOW(), NOW()),
(63, 'Môn học Bộ môn Khoa Toán - Tin học 3 3', 'SUBJ63', 21, NOW(), NOW()),
(64, 'Môn học Bộ môn Khoa Vật lý - Vật lý Kỹ thuật 1 1', 'SUBJ64', 22, NOW(), NOW()),
(65, 'Môn học Bộ môn Khoa Vật lý - Vật lý Kỹ thuật 1 2', 'SUBJ65', 22, NOW(), NOW()),
(66, 'Môn học Bộ môn Khoa Vật lý - Vật lý Kỹ thuật 1 3', 'SUBJ66', 22, NOW(), NOW()),
(67, 'Môn học Bộ môn Khoa Vật lý - Vật lý Kỹ thuật 2 1', 'SUBJ67', 23, NOW(), NOW()),
(68, 'Môn học Bộ môn Khoa Vật lý - Vật lý Kỹ thuật 2 2', 'SUBJ68', 23, NOW(), NOW()),
(69, 'Môn học Bộ môn Khoa Vật lý - Vật lý Kỹ thuật 2 3', 'SUBJ69', 23, NOW(), NOW()),
(70, 'Môn học Bộ môn Khoa Vật lý - Vật lý Kỹ thuật 3 1', 'SUBJ70', 24, NOW(), NOW()),
(71, 'Môn học Bộ môn Khoa Vật lý - Vật lý Kỹ thuật 3 2', 'SUBJ71', 24, NOW(), NOW()),
(72, 'Môn học Bộ môn Khoa Vật lý - Vật lý Kỹ thuật 3 3', 'SUBJ72', 24, NOW(), NOW()),
(73, 'Môn học Bộ môn Khoa Hóa học 1 1', 'SUBJ73', 25, NOW(), NOW()),
(74, 'Môn học Bộ môn Khoa Hóa học 1 2', 'SUBJ74', 25, NOW(), NOW()),
(75, 'Môn học Bộ môn Khoa Hóa học 1 3', 'SUBJ75', 25, NOW(), NOW()),
(76, 'Môn học Bộ môn Khoa Hóa học 2 1', 'SUBJ76', 26, NOW(), NOW()),
(77, 'Môn học Bộ môn Khoa Hóa học 2 2', 'SUBJ77', 26, NOW(), NOW()),
(78, 'Môn học Bộ môn Khoa Hóa học 2 3', 'SUBJ78', 26, NOW(), NOW()),
(79, 'Môn học Bộ môn Khoa Hóa học 3 1', 'SUBJ79', 27, NOW(), NOW()),
(80, 'Môn học Bộ môn Khoa Hóa học 3 2', 'SUBJ80', 27, NOW(), NOW()),
(81, 'Môn học Bộ môn Khoa Hóa học 3 3', 'SUBJ81', 27, NOW(), NOW())
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 8. Documents Seed
INSERT INTO `documents` (
    `id`, `title`, `description`, `file_url`, `file_size`, `file_type`, 
    `uploader_id`, `university_id`, `faculty_id`, `subject_id`, 
    `like_count`, `download_count`, `view_count`, `comment_count`, `page_count`, `is_public`, `status`, `created_at`, `updated_at`
) VALUES
('doc-001', 'Giáo trình Nhập môn Lập trình Java 17', 'Bài giảng chi tiết về ngôn ngữ Java, OOP và Spring Boot Framework.', 'https://example.com/java-tutorial.pdf', 1024500, 'pdf', 'usr-admin-001', 1, 1, 1, 45, 120, 530, 5, 150, 1, 'COMPLETED', NOW(), NOW()),
('doc-002', 'Đề thi và Đáp án Cấu trúc Dữ liệu 2023', 'Bộ đề thi giữa kỳ và cuối kỳ môn Cấu trúc dữ liệu có lời giải.', 'https://example.com/dsa-exam-2023.pdf', 2048000, 'pdf', 'usr-user-002', 1, 1, 2, 30, 85, 410, 2, 20, 1, 'COMPLETED', NOW(), NOW())
ON DUPLICATE KEY UPDATE `title` = VALUES(`title`);
