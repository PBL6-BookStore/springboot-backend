package com.pbl6.bookstore.payload.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lkadai0801
 * @since 28/10/2022
 */

@Getter
@Setter
@Builder(builderMethodName = "newBuilder", setterPrefix = "set")
public class ListBookRequest extends AbstractPageRequest{
    private String searchTerm;
    private Long categoryId;
    private Boolean allInOne;
}
