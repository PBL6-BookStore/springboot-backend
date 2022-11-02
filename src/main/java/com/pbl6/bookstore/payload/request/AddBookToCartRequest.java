package com.pbl6.bookstore.payload.request;

import lombok.Data;

/**
 * @author lkadai0801
 * @since 02/11/2022
 */

@Data
public class AddBookToCartRequest {
    private Long bookId;
    private Integer quantity;
}
