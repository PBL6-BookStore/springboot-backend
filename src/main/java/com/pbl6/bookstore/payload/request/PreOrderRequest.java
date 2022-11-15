package com.pbl6.bookstore.payload.request;

import lombok.*;

import java.util.List;

/**
 * @author lkadai0801
 * @since 13/11/2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreOrderRequest {
    private List<Long> cartDetailId;
    private Long discountId;
}
