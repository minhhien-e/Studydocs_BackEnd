package com.studydocs.modules.user.service;

import com.studydocs.modules.user.dto.LoginRequest;
import com.studydocs.modules.user.dto.UserDto;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    UserDto getCurrentUser(String userId);
    UserDto getUserById(String userId);
    UserDto updateProfile(String userId, LoginRequest.UpdateProfile request);
    UserDto updateProfileImage(String userId, MultipartFile file);
    List<UserDto> searchUsers(String query);
    void requestUpdateEmail(String userId, String newEmail);
    void verifyAndUpdateEmail(String userId, String token);
}
