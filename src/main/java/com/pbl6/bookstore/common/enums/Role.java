package com.pbl6.bookstore.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lkadai0801
 * @since 06/11/2022
 */

@AllArgsConstructor
@Getter
public enum Role {
    ROLE_USER(1L, "ROLE_USER"),
    ROLE_ADMIN(2L, "ROLE_ADMIN");

    private final Long id;
    private final String name;
}
