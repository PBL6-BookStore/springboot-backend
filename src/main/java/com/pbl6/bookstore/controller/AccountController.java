package com.pbl6.bookstore.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lkadai0801
 * @since 02/11/2022
 */

@Tag(name = "Account", description = "Account APIs")
@RestController
@RequestMapping("/accounts")
public class AccountController {
}
