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
(2, 'Khoa Công nghệ Phần mềm', 2, NOW(), NOW()),
(3, 'Khoa Phân tích Dữ liệu', 3, NOW(), NOW())
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 6. Departments Seed
INSERT INTO `departments` (`id`, `name`, `faculty_id`, `created_at`, `updated_at`) VALUES
(1, 'Bộ môn Công nghệ Phần mềm', 1, NOW(), NOW()),
(2, 'Bộ môn Hệ thống Thông tin', 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 7. Subjects Seed
INSERT INTO `subjects` (`id`, `name`, `code`, `department_id`, `created_at`, `updated_at`) VALUES
(1, 'Nhập môn Lập trình Java', 'CO1023', 1, NOW(), NOW()),
(2, 'Cấu trúc Dữ liệu và Giải thuật', 'CO2003', 1, NOW(), NOW()),
(3, 'Cơ sở Dữ liệu', 'CO2013', 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 8. Documents Seed
INSERT INTO `documents` (
    `id`, `title`, `description`, `file_url`, `file_size`, `file_type`, 
    `uploader_id`, `university_id`, `faculty_id`, `subject_id`, 
    `like_count`, `download_count`, `view_count`, `is_public`, `created_at`, `updated_at`
) VALUES
('doc-001', 'Giáo trình Nhập môn Lập trình Java 17', 'Bài giảng chi tiết về ngôn ngữ Java, OOP và Spring Boot Framework.', 'https://example.com/java-tutorial.pdf', 1024500, 'pdf', 'usr-admin-001', 1, 1, 1, 45, 120, 530, 1, NOW(), NOW()),
('doc-002', 'Đề thi và Đáp án Cấu trúc Dữ liệu 2023', 'Bộ đề thi giữa kỳ và cuối kỳ môn Cấu trúc dữ liệu có lời giải.', 'https://example.com/dsa-exam-2023.pdf', 2048000, 'pdf', 'usr-user-002', 1, 1, 2, 30, 85, 410, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE `title` = VALUES(`title`);
