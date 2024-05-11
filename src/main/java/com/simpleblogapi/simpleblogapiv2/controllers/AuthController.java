package com.simpleblogapi.simpleblogapiv2.controllers;

import com.nimbusds.jose.JOSEException;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.IntrospectRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.LoginRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.LogoutRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.UserCreateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.ApiResponse;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.IntrospectResponse;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.LoginResponse;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.UserResponse;
import com.simpleblogapi.simpleblogapiv2.services.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v2/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid
                                                            LoginRequest request) {
        LoginResponse authenticate = authService.login(request);
        ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                .message("Login successfully")
                .result(authenticate)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody @Valid UserCreateRequest request) {
        UserResponse user = authService.register(request);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .message("User registered successfully")
                .result(user)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestBody @Valid LogoutRequest request) throws ParseException, JOSEException {
        authService.logout(request);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Logged out successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/introspect")
    public ResponseEntity<ApiResponse<IntrospectResponse>> introspect(@RequestBody @Valid IntrospectRequest request) throws ParseException, JOSEException {
        IntrospectResponse introspect= authService.introspect(request);
        ApiResponse<IntrospectResponse> response = ApiResponse.<IntrospectResponse>builder()
                .result(introspect)
                .build();
        return ResponseEntity.ok(response);
    }
}
