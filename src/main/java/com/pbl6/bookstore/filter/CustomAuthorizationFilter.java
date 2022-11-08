package com.pbl6.bookstore.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl6.bookstore.common.constant.Constant;
import com.pbl6.bookstore.common.enums.ErrorCode;
import com.pbl6.bookstore.payload.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author vndat00
 * @since 05/11/2022
 */

@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Value("${app.security.secret.key}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals(Constant.LOGIN_PATH) || request.getServletPath().equals(Constant.REFRESH_TOKEN_PATH)){
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(Constant.PREFIX_TOKEN)) {
                try {
                    String token = authorizationHeader.substring(Constant.PREFIX_TOKEN.length());
                    Algorithm algorithm = Algorithm.HMAC256(Constant.BYTE_CODE.getBytes());
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    //Neu token het han, ngoai le se xuan hien o day
                    DecodedJWT decodedJWT = verifier.verify(token);
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<GrantedAuthority> authorities = new ArrayList<>();

                    stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception exception) {
                    log.info("Error logging in: {} ", exception.getMessage());
                    response.setHeader("error", exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    var responseObj = Response.newBuilder()
                                    .setSuccess(false)
                                    .setErrorCode(ErrorCode.UNAUTHORIZED)
                                    .setMessage("Invalid token " + request.getHeader(HttpHeaders.AUTHORIZATION))
                                    .setException(exception.getClass().getSimpleName())
                                    .build();
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), responseObj);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
