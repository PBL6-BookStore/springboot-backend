package com.pbl6.bookstore.payload.response.cart;

import com.pbl6.bookstore.payload.response.book.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author lkadai0801
 * @since 04/11/2022
 */

@Getter
@Setter
@SuperBuilder(builderMethodName = "newBuilder", setterPrefix = "set")
@AllArgsConstructor
@NoArgsConstructor
public class ListCartDetailDTO {
    private Long totalElement;
    private Long total;
    private Long cartId;
    private List<CartDetailDTO> cartDetails;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder(builderMethodName = "newBuilder", setterPrefix = "set")
    public static class CartDetailDTO {
        private Long id;
        private BookDTO book;
        private Integer quantity;
    }
}
