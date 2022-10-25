package com.pbl6.bookstore.controller;

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
public class TestController {
    @GetMapping({
            "/",
            "/test"
    })
    public ResponseEntity<Map<String, Boolean>> testController(){
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
