package com.pbl6.bookstore.payload.response.discount;

import lombok.*;

/**
 * @author lkadai0801
 * @since 12/11/2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder", setterPrefix = "set")
public class DiscountDTO {
    private Long id;
    private String code;
    private String description;
    private String startDate;
    private String endDate;
    private String name;
    private Integer value;
}
