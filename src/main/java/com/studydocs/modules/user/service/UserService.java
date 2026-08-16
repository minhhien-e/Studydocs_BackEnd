package com.studydocs.modules.user.service;

import com.studydocs.modules.user.dto.LoginRequest;
import com.studydocs.modules.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getCurrentUser(String userId);
    UserDto getUserById(String userId);
    UserDto updateProfile(String userId, LoginRequest.UpdateProfile request);
    List<UserDto> searchUsers(String query);
}
