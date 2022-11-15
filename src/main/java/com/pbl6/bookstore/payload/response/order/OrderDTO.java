package com.pbl6.bookstore.payload.response.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pbl6.bookstore.payload.response.discount.DiscountDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author lkadai0801
 * @since 13/11/2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(builderMethodName = "newBuilder", setterPrefix = "set")
public class OrderDTO {
    private Long totalItem;
    private Long totalMoney;
    private List<OrderItems> items;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private DiscountDTO discountDTO;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder(builderMethodName = "newBuilder", setterPrefix = "set")
    public static class OrderItems{
        private Long bookId;
        private String bookTitle;
        private Integer quantity;
        private String image;
        private String author;
        private String price;
    }
}
