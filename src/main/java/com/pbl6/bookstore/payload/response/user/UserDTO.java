package com.pbl6.bookstore.payload.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author lkadai0801
 * @since 02/11/2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(builderMethodName = "newBuilder", setterPrefix = "set")
public class UserDTO {
    private String username;
    private String email;
    private String password;
}