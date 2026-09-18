-- 1. Universities Seed
INSERT INTO `universities` (`id`, `name`, `code`, `logo_url`, `address`, `created_at`, `updated_at`) VALUES
(4, 'Trường Đại học Ngoại thương Cơ sở 2', 'FTU2', 'https://example.com/logo-ftu2.png', '15 Đường D5, Bình Thạnh, TP.HCM', NOW(), NOW()),
(5, 'Trường Đại học Kinh tế TP.HCM', 'UEH', 'https://example.com/logo-ueh.png', '59C Nguyễn Đình Chiểu, Q.3, TP.HCM', NOW(), NOW())
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 2. Faculties Seed
INSERT INTO `faculties` (`id`, `name`, `university_id`, `created_at`, `updated_at`) VALUES
(10, 'Khoa Quản trị Kinh doanh', 4, NOW(), NOW()),
(11, 'Khoa Tài chính Ngân hàng', 4, NOW(), NOW()),
(12, 'Khoa Kinh tế Quốc tế', 4, NOW(), NOW()),
(13, 'Khoa Kế toán', 5, NOW(), NOW()),
(14, 'Khoa Công nghệ thông tin kinh doanh', 5, NOW(), NOW()),
(15, 'Khoa Toán Thống kê', 5, NOW(), NOW())
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 3. Departments Seed
INSERT INTO `departments` (`id`, `name`, `faculty_id`, `created_at`, `updated_at`) VALUES
(28, 'Bộ môn Khoa Quản trị Kinh doanh 1', 10, NOW(), NOW()),
(29, 'Bộ môn Khoa Quản trị Kinh doanh 2', 10, NOW(), NOW()),
(30, 'Bộ môn Khoa Quản trị Kinh doanh 3', 10, NOW(), NOW()),
(31, 'Bộ môn Khoa Tài chính Ngân hàng 1', 11, NOW(), NOW()),
(32, 'Bộ môn Khoa Tài chính Ngân hàng 2', 11, NOW(), NOW()),
(33, 'Bộ môn Khoa Tài chính Ngân hàng 3', 11, NOW(), NOW()),
(34, 'Bộ môn Khoa Kinh tế Quốc tế 1', 12, NOW(), NOW()),
(35, 'Bộ môn Khoa Kinh tế Quốc tế 2', 12, NOW(), NOW()),
(36, 'Bộ môn Khoa Kinh tế Quốc tế 3', 12, NOW(), NOW()),
(37, 'Bộ môn Khoa Kế toán 1', 13, NOW(), NOW()),
(38, 'Bộ môn Khoa Kế toán 2', 13, NOW(), NOW()),
(39, 'Bộ môn Khoa Kế toán 3', 13, NOW(), NOW()),
(40, 'Bộ môn Khoa Công nghệ thông tin kinh doanh 1', 14, NOW(), NOW()),
(41, 'Bộ môn Khoa Công nghệ thông tin kinh doanh 2', 14, NOW(), NOW()),
(42, 'Bộ môn Khoa Công nghệ thông tin kinh doanh 3', 14, NOW(), NOW()),
(43, 'Bộ môn Khoa Toán Thống kê 1', 15, NOW(), NOW()),
(44, 'Bộ môn Khoa Toán Thống kê 2', 15, NOW(), NOW()),
(45, 'Bộ môn Khoa Toán Thống kê 3', 15, NOW(), NOW())
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

-- 4. Subjects Seed
INSERT INTO `subjects` (`id`, `name`, `code`, `department_id`, `created_at`, `updated_at`) VALUES
(82, 'Môn học Bộ môn Khoa Quản trị Kinh doanh 1 1', 'SUBJ82', 28, NOW(), NOW()),
(83, 'Môn học Bộ môn Khoa Quản trị Kinh doanh 1 2', 'SUBJ83', 28, NOW(), NOW()),
(84, 'Môn học Bộ môn Khoa Quản trị Kinh doanh 1 3', 'SUBJ84', 28, NOW(), NOW()),
(85, 'Môn học Bộ môn Khoa Quản trị Kinh doanh 2 1', 'SUBJ85', 29, NOW(), NOW()),
(86, 'Môn học Bộ môn Khoa Quản trị Kinh doanh 2 2', 'SUBJ86', 29, NOW(), NOW()),
(87, 'Môn học Bộ môn Khoa Quản trị Kinh doanh 2 3', 'SUBJ87', 29, NOW(), NOW()),
(88, 'Môn học Bộ môn Khoa Quản trị Kinh doanh 3 1', 'SUBJ88', 30, NOW(), NOW()),
(89, 'Môn học Bộ môn Khoa Quản trị Kinh doanh 3 2', 'SUBJ89', 30, NOW(), NOW()),
(90, 'Môn học Bộ môn Khoa Quản trị Kinh doanh 3 3', 'SUBJ90', 30, NOW(), NOW()),
(91, 'Môn học Bộ môn Khoa Tài chính Ngân hàng 1 1', 'SUBJ91', 31, NOW(), NOW()),
(92, 'Môn học Bộ môn Khoa Tài chính Ngân hàng 1 2', 'SUBJ92', 31, NOW(), NOW()),
(93, 'Môn học Bộ môn Khoa Tài chính Ngân hàng 1 3', 'SUBJ93', 31, NOW(), NOW()),
(94, 'Môn học Bộ môn Khoa Tài chính Ngân hàng 2 1', 'SUBJ94', 32, NOW(), NOW()),
(95, 'Môn học Bộ môn Khoa Tài chính Ngân hàng 2 2', 'SUBJ95', 32, NOW(), NOW()),
(96, 'Môn học Bộ môn Khoa Tài chính Ngân hàng 2 3', 'SUBJ96', 32, NOW(), NOW()),
(97, 'Môn học Bộ môn Khoa Tài chính Ngân hàng 3 1', 'SUBJ97', 33, NOW(), NOW()),
(98, 'Môn học Bộ môn Khoa Tài chính Ngân hàng 3 2', 'SUBJ98', 33, NOW(), NOW()),
(99, 'Môn học Bộ môn Khoa Tài chính Ngân hàng 3 3', 'SUBJ99', 33, NOW(), NOW()),
(100, 'Môn học Bộ môn Khoa Kinh tế Quốc tế 1 1', 'SUBJ100', 34, NOW(), NOW()),
(101, 'Môn học Bộ môn Khoa Kinh tế Quốc tế 1 2', 'SUBJ101', 34, NOW(), NOW()),
(102, 'Môn học Bộ môn Khoa Kinh tế Quốc tế 1 3', 'SUBJ102', 34, NOW(), NOW()),
(103, 'Môn học Bộ môn Khoa Kinh tế Quốc tế 2 1', 'SUBJ103', 35, NOW(), NOW()),
(104, 'Môn học Bộ môn Khoa Kinh tế Quốc tế 2 2', 'SUBJ104', 35, NOW(), NOW()),
(105, 'Môn học Bộ môn Khoa Kinh tế Quốc tế 2 3', 'SUBJ105', 35, NOW(), NOW()),
(106, 'Môn học Bộ môn Khoa Kinh tế Quốc tế 3 1', 'SUBJ106', 36, NOW(), NOW()),
(107, 'Môn học Bộ môn Khoa Kinh tế Quốc tế 3 2', 'SUBJ107', 36, NOW(), NOW()),
(108, 'Môn học Bộ môn Khoa Kinh tế Quốc tế 3 3', 'SUBJ108', 36, NOW(), NOW()),
(109, 'Môn học Bộ môn Khoa Kế toán 1 1', 'SUBJ109', 37, NOW(), NOW()),
(110, 'Môn học Bộ môn Khoa Kế toán 1 2', 'SUBJ110', 37, NOW(), NOW()),
(111, 'Môn học Bộ môn Khoa Kế toán 1 3', 'SUBJ111', 37, NOW(), NOW()),
(112, 'Môn học Bộ môn Khoa Kế toán 2 1', 'SUBJ112', 38, NOW(), NOW()),
(113, 'Môn học Bộ môn Khoa Kế toán 2 2', 'SUBJ113', 38, NOW(), NOW()),
(114, 'Môn học Bộ môn Khoa Kế toán 2 3', 'SUBJ114', 38, NOW(), NOW()),
(115, 'Môn học Bộ môn Khoa Kế toán 3 1', 'SUBJ115', 39, NOW(), NOW()),
(116, 'Môn học Bộ môn Khoa Kế toán 3 2', 'SUBJ116', 39, NOW(), NOW()),
(117, 'Môn học Bộ môn Khoa Kế toán 3 3', 'SUBJ117', 39, NOW(), NOW()),
(118, 'Môn học Bộ môn Khoa Công nghệ thông tin kinh doanh 1 1', 'SUBJ118', 40, NOW(), NOW()),
(119, 'Môn học Bộ môn Khoa Công nghệ thông tin kinh doanh 1 2', 'SUBJ119', 40, NOW(), NOW()),
(120, 'Môn học Bộ môn Khoa Công nghệ thông tin kinh doanh 1 3', 'SUBJ120', 40, NOW(), NOW()),
(121, 'Môn học Bộ môn Khoa Công nghệ thông tin kinh doanh 2 1', 'SUBJ121', 41, NOW(), NOW()),
(122, 'Môn học Bộ môn Khoa Công nghệ thông tin kinh doanh 2 2', 'SUBJ122', 41, NOW(), NOW()),
(123, 'Môn học Bộ môn Khoa Công nghệ thông tin kinh doanh 2 3', 'SUBJ123', 41, NOW(), NOW()),
(124, 'Môn học Bộ môn Khoa Công nghệ thông tin kinh doanh 3 1', 'SUBJ124', 42, NOW(), NOW()),
(125, 'Môn học Bộ môn Khoa Công nghệ thông tin kinh doanh 3 2', 'SUBJ125', 42, NOW(), NOW()),
(126, 'Môn học Bộ môn Khoa Công nghệ thông tin kinh doanh 3 3', 'SUBJ126', 42, NOW(), NOW()),
(127, 'Môn học Bộ môn Khoa Toán Thống kê 1 1', 'SUBJ127', 43, NOW(), NOW()),
(128, 'Môn học Bộ môn Khoa Toán Thống kê 1 2', 'SUBJ128', 43, NOW(), NOW()),
(129, 'Môn học Bộ môn Khoa Toán Thống kê 1 3', 'SUBJ129', 43, NOW(), NOW()),
(130, 'Môn học Bộ môn Khoa Toán Thống kê 2 1', 'SUBJ130', 44, NOW(), NOW()),
(131, 'Môn học Bộ môn Khoa Toán Thống kê 2 2', 'SUBJ131', 44, NOW(), NOW()),
(132, 'Môn học Bộ môn Khoa Toán Thống kê 2 3', 'SUBJ132', 44, NOW(), NOW()),
(133, 'Môn học Bộ môn Khoa Toán Thống kê 3 1', 'SUBJ133', 45, NOW(), NOW()),
(134, 'Môn học Bộ môn Khoa Toán Thống kê 3 2', 'SUBJ134', 45, NOW(), NOW()),
(135, 'Môn học Bộ môn Khoa Toán Thống kê 3 3', 'SUBJ135', 45, NOW(), NOW())
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);
