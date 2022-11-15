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
public class AddDiscountRequest {
    private String name;
    private String code;
    private String description;
    private String startDate;
    private String endDate;
    private Integer value;
}
