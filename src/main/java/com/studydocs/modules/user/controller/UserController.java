package com.studydocs.modules.user.controller;

import com.studydocs.modules.user.dto.LoginRequest;
import com.studydocs.modules.user.dto.UserDto;
import com.studydocs.modules.user.service.UserService;
import com.studydocs.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserDto> getMyProfile(Authentication authentication) {
        String userId = authentication != null ? authentication.getName() : "anonymous";
        return ApiResponse.success(userService.getCurrentUser(userId));
    }

    @PutMapping({"/me", "/{userId}", "/{userId}/info"})
    public ApiResponse<UserDto> updateMyProfile(Authentication authentication,
                                                @PathVariable(required = false) String userId,
                                                @RequestBody LoginRequest.UpdateProfile request) {
        String currentUserId = authentication != null ? authentication.getName() : userId;
        return ApiResponse.success(userService.updateProfile(currentUserId, request));
    }

    @PostMapping(value = "/{userId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<UserDto> updateProfileImage(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        return ApiResponse.success(userService.updateProfileImage(userId, file));
    }

    @GetMapping({"/{userId}", "/{userId}/other", "/public/profile/{userId}"})
    public ApiResponse<UserDto> getUserProfile(@PathVariable String userId) {
        return ApiResponse.success(userService.getUserById(userId));
    }

    @GetMapping("/search")
    public ApiResponse<List<UserDto>> searchUsers(@RequestParam("q") String query) {
        return ApiResponse.success(userService.searchUsers(query));
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable String userId) {
        return ApiResponse.success("User deleted successfully");
    }
}
