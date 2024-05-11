package com.simpleblogapi.simpleblogapiv2.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private String fullname;
    private String password;
    List<String> roles;
}
