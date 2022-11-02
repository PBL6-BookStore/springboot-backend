package com.pbl6.bookstore.payload.response.category;

import lombok.*;

/**
 * @author lkadai0801
 * @since 01/11/2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder", setterPrefix = "set")
public class CategoryResponse {
    private Long id;
    private String name;
}
