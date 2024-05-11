package com.simpleblogapi.simpleblogapiv2.services;

import com.simpleblogapi.simpleblogapiv2.dtos.responses.RoleResponse;
import com.simpleblogapi.simpleblogapiv2.exceptions.AppException;
import com.simpleblogapi.simpleblogapiv2.exceptions.ErrorCode;
import com.simpleblogapi.simpleblogapiv2.mappers.RoleMapper;
import com.simpleblogapi.simpleblogapiv2.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toRoleResponse).toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse getRoleById(String name) {
        return roleMapper.toRoleResponse(roleRepository.findById(name).orElseThrow(
                () -> new AppException(ErrorCode.ROLE_NOT_FOUND)
        ));
    }
}
