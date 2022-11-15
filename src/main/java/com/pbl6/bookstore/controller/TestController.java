package com.pbl6.bookstore.controller;

import com.pbl6.bookstore.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lkadai0801
 * @since 25/10/2022
 */

@RestController
@Hidden
@RequiredArgsConstructor
public class TestController {
    private final SecurityUtils securityUtils;

    @GetMapping({
            "/",
            "/test"
    })
    public ResponseEntity<Map<String, Boolean>> testController(){
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-user")
    public ResponseEntity<?> getMyUser(){
        return ResponseEntity.ok(securityUtils.getPrincipal());
    }
}
