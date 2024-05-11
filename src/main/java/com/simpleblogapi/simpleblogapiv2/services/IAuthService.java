package com.simpleblogapi.simpleblogapiv2.services;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.IntrospectRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.LoginRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.LogoutRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.UserCreateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.IntrospectResponse;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.LoginResponse;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.UserResponse;


import java.text.ParseException;

public interface IAuthService {
    LoginResponse login(LoginRequest request);
    UserResponse register (UserCreateRequest request);
    void logout(LogoutRequest request) throws ParseException, JOSEException;
     IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException;
     SignedJWT verifyToken(String token) throws JOSEException, ParseException;

}
