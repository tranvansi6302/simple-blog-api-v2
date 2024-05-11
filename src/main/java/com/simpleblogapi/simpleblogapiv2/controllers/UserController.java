package com.simpleblogapi.simpleblogapiv2.controllers;

import com.simpleblogapi.simpleblogapiv2.dtos.requests.MeUpdateRequest;

import com.simpleblogapi.simpleblogapiv2.dtos.requests.UserUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.ApiResponse;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.UserResponse;
import com.simpleblogapi.simpleblogapiv2.services.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;


    @GetMapping("")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(id))
                .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable String id, @RequestBody @Valid UserUpdateRequest request) {
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(id, request))
                .message("User updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMe(@RequestBody @Valid MeUpdateRequest request) {
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .result(userService.updateMe(request))
                .message("User updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe() {
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .result(userService.getMe())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .message("User deleted successfully")
                .build());
    }
}
