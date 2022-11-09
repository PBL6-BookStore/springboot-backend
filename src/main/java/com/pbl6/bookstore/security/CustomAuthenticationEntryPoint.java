package com.pbl6.bookstore.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl6.bookstore.common.enums.ErrorCode;
import com.pbl6.bookstore.payload.response.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author lkadai0801
 * @since 09/11/2022
 */
@Log4j2
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized: {}", request.getRequestURI());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        var error = Response.newBuilder()
                .setSuccess(false)
                .setMessage(authException.getMessage())
                .setErrorCode(ErrorCode.UNAUTHORIZED)
                .setException(authException.getClass().getSimpleName())
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
