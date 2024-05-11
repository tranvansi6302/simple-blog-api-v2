package com.simpleblogapi.simpleblogapiv2.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class JwtTokenUtil {
    public static  String getRole() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .findFirst().orElseThrow().getAuthority();
    }
}
