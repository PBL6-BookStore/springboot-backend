package com.pbl6.bookstore.payload.request;

import lombok.*;

/**
 * @author lkadai0801
 * @since 12/11/2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllDiscountRequest {
    private Boolean isActive;
    private String code;
}
