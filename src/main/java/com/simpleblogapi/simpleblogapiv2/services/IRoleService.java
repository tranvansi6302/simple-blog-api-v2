package com.simpleblogapi.simpleblogapiv2.services;

import com.simpleblogapi.simpleblogapiv2.dtos.responses.RoleResponse;

import java.util.List;

public interface IRoleService {
    List<RoleResponse> getAllRoles();
    RoleResponse getRoleById(String name);
}
