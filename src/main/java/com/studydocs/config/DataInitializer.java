package com.studydocs.config;

import com.studydocs.modules.academic.entity.*;
import com.studydocs.modules.academic.repository.*;
import com.studydocs.modules.user.entity.RoleEntity;
import com.studydocs.modules.user.entity.UserEntity;
import com.studydocs.modules.user.repository.RoleRepository;
import com.studydocs.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UniversityRepository universityRepository;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final SubjectRepository subjectRepository;
    private final DocumentRepository documentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Checking data initialization...");

        // 1. Init Roles
        RoleEntity adminRole = roleRepository.findById("ADMIN")
                .orElseGet(() -> roleRepository.save(RoleEntity.builder().name("ADMIN").description("Administrator role").build()));
        RoleEntity userRole = roleRepository.findById("USER")
                .orElseGet(() -> roleRepository.save(RoleEntity.builder().name("USER").description("Standard User role").build()));

        // 2. Init Admin User
        if (!userRepository.existsByEmail("admin@studydocs.com")) {
            UserEntity admin = UserEntity.builder()
                    .email("admin@studydocs.com")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("StudyDocs Administrator")
                    .username("admin")
                    .phoneNumber("0987654321")
                    .gender("Male")
                    .address("268 Lý Thường Kiệt, Q.10, TP.HCM")
                    .dateOfBirth(LocalDateTime.of(2000, 1, 1, 0, 0))
                    .universityName("Trường Đại học Bách Khoa - ĐHQG TP.HCM")
                    .roles(Set.of(adminRole, userRole))
                    .build();
            userRepository.save(admin);
            log.info("Created default admin user: admin@studydocs.com");
        }

        // 3. Init Sample Universities & Academic Entities
        if (universityRepository.count() == 0) {
            UniversityEntity uni1 = universityRepository.save(UniversityEntity.builder()
                    .name("Trường Đại học Bách Khoa - ĐHQG TP.HCM")
                    .code("HCMUT")
                    .address("268 Lý Thường Kiệt, Q.10, TP.HCM")
                    .build());

            UniversityEntity uni2 = universityRepository.save(UniversityEntity.builder()
                    .name("Trường Đại học Công nghệ Thông tin - ĐHQG TP.HCM")
                    .code("UIT")
                    .address("Khu phố 6, P. Linh Trung, TP. Thủ Đức")
                    .build());

            FacultyEntity fac1 = facultyRepository.save(FacultyEntity.builder()
                    .name("Khoa Khoa học và Kỹ thuật Máy tính")
                    .universityId(uni1.getId())
                    .build());

            DepartmentEntity dep1 = departmentRepository.save(DepartmentEntity.builder()
                    .name("Bộ môn Công nghệ Phần mềm")
                    .facultyId(fac1.getId())
                    .build());

            SubjectEntity sub1 = subjectRepository.save(SubjectEntity.builder()
                    .name("Nhập môn Lập trình Java")
                    .code("CO1023")
                    .departmentId(dep1.getId())
                    .build());

            // 4. Sample Document
            UserEntity adminUser = userRepository.findByEmail("admin@studydocs.com").orElse(null);
            if (adminUser != null && documentRepository.count() == 0) {
                documentRepository.save(DocumentEntity.builder()
                        .title("Giáo trình Nhập môn Lập trình Java")
                        .description("Tài liệu bài giảng chi tiết về ngôn ngữ lập trình Java 17 và Spring Boot.")
                        .fileUrl("https://example.com/java-tutorial.pdf")
                        .fileSize(1024500L)
                        .fileType("pdf")
                        .uploaderId(adminUser.getId())
                        .universityId(uni1.getId())
                        .facultyId(fac1.getId())
                        .subjectId(sub1.getId())
                        .likeCount(25)
                        .downloadCount(100)
                        .viewCount(500)
                        .isPublic(true)
                        .build());
            }
            log.info("Initialized sample academic data successfully.");
        }
    }
}
