package studydoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studydoc.bus.SimpleUserCommandBus;
import studydoc.request.RegisterRequest;
import studydoc.response.ApiResponse;
import studydoc.mapper.RequestMapper;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final SimpleUserCommandBus commandBus;
    private final RequestMapper mapper;

    @PostMapping("/register")
    public ApiResponse<?> register(
            @Valid @RequestBody RegisterRequest request) {
        var command = mapper.toRegisterUserCommand(request);
        var result = commandBus.send(command);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(result)).getBody();
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getUserById(@PathVariable String id) {
        var command = studydoc.command.GetUserById.commandOf(id);
        var result = commandBus.send(command);
        return ApiResponse.success(result);
    }

    @GetMapping
    public ApiResponse<?> getAllUsers() {
        var command = studydoc.command.GetAllUsers.commandOf();
        var result = commandBus.send(command);
        return ApiResponse.success(result);
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateUser(
            @PathVariable String id,
            @Valid @RequestBody studydoc.request.UpdateRequest request) {
        var command = mapper.toUpdateUserCommand(id, request);
        var result = commandBus.send(command);
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteUser(@PathVariable String id) {
        var command = studydoc.command.DeleteUser.commandOf(id);
        var result = commandBus.send(command);
        return ApiResponse.success(result);
    }

    @PutMapping("/{id}/info")
    public ApiResponse<?> updateUserInfo(
            @PathVariable String id,
            @Valid @RequestBody studydoc.request.UpdateUserInfoRequest request) {
        var command = mapper.toUpdateUserInfoCommand(id, request);
        var result = commandBus.send(command);
        return ApiResponse.success(result);
    }

    @PatchMapping("/{id}/image")
    public ApiResponse<?> updateUserImage(
            @PathVariable String id,
            @Valid @RequestBody studydoc.request.UpdateUserImageRequest request) {
        var command = mapper.toUpdateUserImageCommand(id, request);
        var result = commandBus.send(command);
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}/other")
    public ApiResponse<?> getOtherUserInfo(@PathVariable String id) {
        var command = studydoc.command.GetOtherUserInfo.commandOf(id);
        var result = commandBus.send(command);
        return ApiResponse.success(result);
    }
}
