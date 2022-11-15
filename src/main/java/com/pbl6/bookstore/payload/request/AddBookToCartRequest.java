package com.pbl6.bookstore.payload.request;

import lombok.Builder;
import lombok.Data;

/**
 * @author lkadai0801
 * @since 02/11/2022
 */

@Data
@Builder(builderMethodName = "newBuilder", setterPrefix = "set")
public class AddBookToCartRequest {
    private Long cartId;
    private Long bookId;
    private Integer quantity;
}
