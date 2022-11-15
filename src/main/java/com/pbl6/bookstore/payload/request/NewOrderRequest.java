package com.pbl6.bookstore.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author lkadai0801
 * @since 05/11/2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewOrderRequest {
    private List<Long> cartDetailIds;
    private Long discountId;
}
