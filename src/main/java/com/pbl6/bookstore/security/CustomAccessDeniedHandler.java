package com.pbl6.bookstore.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author lkadai0801
 * @since 07/11/2022
 */

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // handler exception

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        new ObjectMapper().writeValue(response.getOutputStream(), Map.of("success", false));
        log.info("access denied");
        response.getWriter().write("Noooo");
    }

}
