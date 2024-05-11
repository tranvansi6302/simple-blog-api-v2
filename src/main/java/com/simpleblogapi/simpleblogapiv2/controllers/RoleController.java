package com.simpleblogapi.simpleblogapiv2.controllers;

import com.simpleblogapi.simpleblogapiv2.dtos.responses.ApiResponse;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.CategoryResponse;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.RoleResponse;
import com.simpleblogapi.simpleblogapiv2.services.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/roles")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService roleService;
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        return ResponseEntity.ok(ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAllRoles())
                .build());
    }

    @GetMapping("/{name}")
     public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(  @PathVariable String name) {
          return ResponseEntity.ok(ApiResponse.<RoleResponse>builder()
                 .result(roleService.getRoleById(name))
                 .build());
     }
}
