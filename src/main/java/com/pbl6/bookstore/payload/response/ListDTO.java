package com.pbl6.bookstore.payload.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author lkadai0801
 * @since 26/10/2022
 */

@Getter
@Setter
@SuperBuilder(builderMethodName = "newBuilder", setterPrefix = "set")
public class ListDTO<T> implements Serializable {
    private Long totalElements;
    private List<T> items;
}
