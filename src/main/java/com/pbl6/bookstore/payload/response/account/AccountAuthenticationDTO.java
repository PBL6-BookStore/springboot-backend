package com.pbl6.bookstore.payload.response.account;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author lkadai0801
 * @since 15/11/2022
 */

@Getter
@Setter
@SuperBuilder(builderMethodName = "newBuilder", setterPrefix = "set")
public class AccountAuthenticationDTO extends AccountDTO{
    private Long cartId;
    private Long userId;
}
