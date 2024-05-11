package com.simpleblogapi.simpleblogapiv2.configurations;

import com.simpleblogapi.simpleblogapiv2.dtos.requests.IntrospectRequest;
import com.simpleblogapi.simpleblogapiv2.dtos.responses.IntrospectResponse;
import com.simpleblogapi.simpleblogapiv2.exceptions.ErrorCode;
import com.simpleblogapi.simpleblogapiv2.services.IAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import com.nimbusds.jose.JOSEException;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

    private final IAuthService authService;
    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            IntrospectResponse response = authService.introspect(
                    IntrospectRequest.builder().token(token).build());
            if (!response.isValid()) throw new JwtException(ErrorCode.TOKEN_INVALID.getMessage());
            log.info("Introspect response: {}", response);
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
