package com.pbl6.bookstore.common;

import com.pbl6.bookstore.common.constant.BookStorePermission;
import lombok.*;

import java.util.List;

/**
 * @author lkadai0801
 * @since 12/11/2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "newBuilder", setterPrefix = "set")
public class BookStorePrincipal {
    private String email;
    private String password;
    private Long cartId;
    private Long userId;
    private List<String> roles;

    public Boolean isAdmin(){
        return roles.contains(BookStorePermission.Role.ADMIN);
    }
    public Boolean isUser(){
        return roles.contains(BookStorePermission.Role.USER);
    }
}
