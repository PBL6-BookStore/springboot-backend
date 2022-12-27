package com.pbl6.bookstore.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl6.bookstore.common.constant.Constant;
import com.pbl6.bookstore.common.enums.ErrorCode;
import com.pbl6.bookstore.domain.entity.AccountEntity;
import com.pbl6.bookstore.domain.entity.RoleEntity;
import com.pbl6.bookstore.domain.repository.jpa.AccountRepository;
import com.pbl6.bookstore.exception.ObjectNotFoundException;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.account.AccountAuthenticationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author vndat00
 * @since 05/11/2022
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final long EXPIRE_DURATION_ACCESS_TOKEN = 2 * 60 * 60 * 1000; // 1h
    private static final long EXPIRE_DURATION_REFRESH_TOKEN = 10 * 60 * 60 * 1000; // 10hrs

    private final AccountRepository accountRepository;

    private final AuthenticationManager authenticationManager;

    @Value("${app.security.secret.key}")
    private String secretKey;
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Response<Object> responseObj = Response.<Object>newBuilder()
                .setSuccess(false)
                .setMessage(failed.getMessage())
                .setErrorCode(ErrorCode.AUTHENTICATION_FAILURE)
                .setException(failed.getClass().getSimpleName())
                .build();
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseObj);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            log.info("email is {}", email);
            log.info("Password is {}", password);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            return authenticationManager.authenticate(authToken);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AccountEntity user = (AccountEntity) authentication.getPrincipal();
        var account = accountRepository.findByEmail(user.getUsername()).orElseThrow(() ->
                new ObjectNotFoundException("email", user.getUsername()));
        Algorithm algorithm = Algorithm.HMAC256(Constant.BYTE_CODE.getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_DURATION_ACCESS_TOKEN))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim("account_id", account.getId())
                .withClaim("user_id", account.getUser().getId())
                .withClaim("cart_id", account.getCart().getId())
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_DURATION_REFRESH_TOKEN))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        Map<String, Object> tokens = new HashMap<>();
        tokens.put(Constant.ACCESS_TOKEN, access_token);
        tokens.put(Constant.REFRESH_TOKEN, refresh_token);
        AccountAuthenticationDTO authenticationDTO = AccountAuthenticationDTO.newBuilder()
                        .setEmail(account.getEmail())
                        .setRoles(account.getRoles().stream().map(RoleEntity::getRole).collect(Collectors.toSet()))
                        .setCartId(account.getCart().getId())
                        .setUserId(account.getUser().getId())
                        .build();
        tokens.put("account", authenticationDTO);

        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
