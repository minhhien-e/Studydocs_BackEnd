package studydoc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import studydoc.bus.SimpleUserCommandBus;
import studydoc.mapper.RequestMapper;
import studydoc.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final SimpleUserCommandBus commandBus;
    private final RequestMapper mapper;

    @GetMapping("/me")
    public ApiResponse<?> getCurrentUser() {
        var command = studydoc.command.GetCurrentUser.commandOf();
        var result = commandBus.send(command);
        return ApiResponse.success(result);
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
