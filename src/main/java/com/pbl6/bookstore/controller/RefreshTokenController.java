package com.pbl6.bookstore.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl6.bookstore.common.constant.Constant;
import com.pbl6.bookstore.payload.response.account.AccountDTO;
import com.pbl6.bookstore.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * @author vndat00
 * @since 05/11/2022
 */

@Tag(name = "Token", description = "Tokens API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/token/refresh")
public class RefreshTokenController {
    private static final long EXPIRE_DURATION_ACCESS_TOKEN = 10 * 60 * 1000; // 10mins
//    private static final long EXPIRE_DURATION_REFRESH_TOKEN = 10 * 60 * 60 * 1000; // 10hrs

    private final AccountService accountService;

    @GetMapping
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationToken = request.getHeader(AUTHORIZATION);
        if ( authorizationToken != null && authorizationToken.startsWith(Constant.PREFIX_TOKEN)){
            try {
                String refresh_token = authorizationToken.substring(Constant.PREFIX_TOKEN.length());
                Algorithm algorithm = Algorithm.HMAC256(Constant.BYTE_CODE.getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                // username in this case is email of account
                String username = decodedJWT.getSubject();
                AccountDTO accountDTO = accountService.getAccountByEmail(username).getData();
                String access_token = JWT.create()
                        .withSubject(accountDTO.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_DURATION_ACCESS_TOKEN))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", new ArrayList<>(accountDTO.getRoles()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put(Constant.ACCESS_TOKEN, access_token);
                tokens.put(Constant.REFRESH_TOKEN, refresh_token);

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                log.info("Occured error when refresh token");
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());

                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType("application/json");
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing!");
        }

    }
}
