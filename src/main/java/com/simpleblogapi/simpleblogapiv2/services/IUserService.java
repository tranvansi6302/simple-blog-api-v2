package com.simpleblogapi.simpleblogapiv2.services;

import com.simpleblogapi.simpleblogapiv2.dtos.requests.MeUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.UserCreateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.UserUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.UserResponse;

import java.util.List;

public interface IUserService {
    List<UserResponse> getAllUsers();
    UserResponse updateUser(String id, UserUpdateRequest request);
    UserResponse updateMe(MeUpdateRequest request);
    UserResponse getMe();
    void deleteUser(String id);
    UserResponse getUserById(String id);
}
