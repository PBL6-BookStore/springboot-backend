package com.pbl6.bookstore.util;

import com.pbl6.bookstore.common.BookStorePrincipal;
import com.pbl6.bookstore.domain.entity.AccountEntity;
import com.pbl6.bookstore.domain.entity.RoleEntity;
import com.pbl6.bookstore.domain.repository.jpa.AccountRepository;
import com.pbl6.bookstore.payload.response.account.AccountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @author lkadai0801
 * @since 10/11/2022
 */
@Component
@RequiredArgsConstructor
public class SecurityUtils {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    public BookStorePrincipal getPrincipal(){
        if (SecurityContextHolder.getContext().getAuthentication() != null){
            String email = (String) (SecurityContextHolder.getContext()).getAuthentication().getPrincipal();
            var principal = accountRepository.findByEmail(email).get();
            return BookStorePrincipal.newBuilder()
                    .setEmail(email)
                    .setPassword(principal.getPassword())
                    .setRoles(principal.getRoles().stream()
                            .map(RoleEntity::getRole)
                            .collect(Collectors.toUnmodifiableList()))
                    .setCartId(principal.getCart().getId())
                    .setUserId(principal.getUser().getId())
                    .build();
        }
        return null;
    }
}
