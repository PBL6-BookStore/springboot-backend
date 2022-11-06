package com.pbl6.bookstore.controller;

import com.pbl6.bookstore.payload.request.AccountRequest;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author lkadai0801
 * @since 02/11/2022
 */

@Tag(name = "Account", description = "Account APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public Response<OnlyIdDTO> addAccount(@RequestBody AccountRequest request){
        return accountService.addNewAccount(request);
    }

    @PostMapping("/add-role")
    public ResponseEntity<?> addRoleToAccount(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "role") String role)
    {
        accountService.addRoleToAccount(email, role);
        return ResponseEntity.ok().build();
    }
}
