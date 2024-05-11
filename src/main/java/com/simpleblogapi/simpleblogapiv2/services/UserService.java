package com.simpleblogapi.simpleblogapiv2.services;

import com.simpleblogapi.simpleblogapiv2.dtos.requests.MeUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.UserUpdateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.UserResponse;
import com.simpleblogapi.simpleblogapiv2.entities.Role;
import com.simpleblogapi.simpleblogapiv2.entities.User;
import com.simpleblogapi.simpleblogapiv2.exceptions.AppException;
import com.simpleblogapi.simpleblogapiv2.exceptions.ErrorCode;
import com.simpleblogapi.simpleblogapiv2.mappers.UserMapper;
import com.simpleblogapi.simpleblogapiv2.repositories.RoleRepository;
import com.simpleblogapi.simpleblogapiv2.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String id, UserUpdateRequest request) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (request.getRoles() != null) {
            List<Role> roles = roleRepository.findAllById(request.getRoles());
            user.setRoles(roles);
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (!request.getFullname().isEmpty()) {
            userMapper.toUser(user, request);
        }
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public UserResponse updateMe(MeUpdateRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (!request.getFullname().isEmpty()) {
            user.setFullname(request.getFullname());
        }


        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getMe() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.getEmail().equals("admin@gmail.com")) {
            throw new AppException(ErrorCode.CANNOT_DELETE_ADMIN);
        }
        userRepository.delete(user);
    }

    @Override
    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }
}
