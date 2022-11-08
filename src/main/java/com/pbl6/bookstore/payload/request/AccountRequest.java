package com.pbl6.bookstore.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author vndat00
 * @since 05/11/2022
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(builderMethodName = "newBuilder", setterPrefix = "set")
public class AccountRequest {

    @NotNull
    private String email;
    @NotNull
    private String password;
}
