package com.simpleblogapi.simpleblogapiv2.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.IntrospectRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.LoginRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.LogoutRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.requests.UserCreateRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.IntrospectResponse;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.LoginResponse;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.UserResponse;
import com.simpleblogapi.simpleblogapiv2.entities.LogoutToken;
import com.simpleblogapi.simpleblogapiv2.entities.Role;
import com.simpleblogapi.simpleblogapiv2.entities.User;
import com.simpleblogapi.simpleblogapiv2.exceptions.AppException;
import com.simpleblogapi.simpleblogapiv2.exceptions.ErrorCode;
import com.simpleblogapi.simpleblogapiv2.mappers.UserMapper;
import com.simpleblogapi.simpleblogapiv2.repositories.LogoutTokenRepository;
import com.simpleblogapi.simpleblogapiv2.repositories.RoleRepository;
import com.simpleblogapi.simpleblogapiv2.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LogoutTokenRepository logoutTokenRepository;
    private final UserMapper userMapper;
    @Value("${jwt.signerKey}")
    protected String signerKey;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_OR_PASSWORD_INCORRECT));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.EMAIL_OR_PASSWORD_INCORRECT);
        }
        String token = generateToken(user);
        return LoginResponse.builder()
                .token(token)
                .user(userMapper.toUserResponse(user))
                .build();
    }

    @Override
    public UserResponse register(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTS);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // Default role is USER
        List<Role> roles = roleRepository.findAllById(Collections.singleton(Role.USER));
        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken());
        String jti = signedJWT.getJWTClaimsSet().getJWTID();
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        LogoutToken logoutToken = LogoutToken.builder()
                .id(jti)
                .expiryDate(expirationTime)
                .build();
        logoutTokenRepository.save(logoutToken);
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("simple-blog-api-v2")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.DAYS))) // 1 day
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(
                header,
                payload
        );
        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Error signing JWT token", e);
            throw new RuntimeException(e);
        }

    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> stringJoiner.add(role.getName()));
        }
        return stringJoiner.toString();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        String token = request.getToken();
        boolean isTokenValid = true;
        try {
            verifyToken(token);
        } catch (AppException e) {
            isTokenValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isTokenValid)
                .build();
    }

    @Override
    public SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier jwsVerifier = new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(jwsVerifier);
        if (!verified || !expirationTime.after(new Date())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (logoutTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return signedJWT;

    }

}
