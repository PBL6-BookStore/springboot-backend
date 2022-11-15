package com.pbl6.bookstore.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lkadai0801
 * @since 06/11/2022
 */

@AllArgsConstructor
@Getter
public enum OrderStatus {
    UNPAID(1L, "UNPAID"),
    PAID(2L, "PAID"),
    CANCELLED(3L, "CANCELLED");

    private final Long id;
    private final String code;
}
