package com.pbl6.bookstore.payload.response.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * @author vndat00
 * @since 05/11/2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder( builderMethodName = "newBuilder", setterPrefix = "set")
public class AccountDTO {
    private String email;
    private String password;
    private Set<String> roles;
}
