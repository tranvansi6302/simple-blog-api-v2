package com.simpleblogapi.simpleblogapiv2.mappers;

import com.simpleblogapi.simpleblogapiv2.dtos.responses.RoleResponse;
import com.simpleblogapi.simpleblogapiv2.entities.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);
}
