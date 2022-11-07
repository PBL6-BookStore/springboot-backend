package com.pbl6.bookstore.service.impl;

import com.pbl6.bookstore.common.constant.BookStorePermission;
import com.pbl6.bookstore.domain.entity.AccountEntity;
import com.pbl6.bookstore.domain.entity.RoleEntity;
import com.pbl6.bookstore.domain.entity.UserEntity;
import com.pbl6.bookstore.domain.repository.jpa.AccountRepository;
import com.pbl6.bookstore.domain.repository.jpa.RoleRepository;
import com.pbl6.bookstore.domain.repository.jpa.UserRepository;
import com.pbl6.bookstore.exception.ObjectNotFoundException;
import com.pbl6.bookstore.payload.request.AccountRequest;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.account.AccountDTO;
import com.pbl6.bookstore.service.AccountService;
import com.pbl6.bookstore.service.CartService;
import com.pbl6.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author vndat00
 * @since 05/11/2022
 */

@RequiredArgsConstructor
@Service
@Transactional
@Log4j2
public class AccountServiceImpl implements AccountService, UserDetailsService {
    private static String USER = BookStorePermission.Role.USER;

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final CartService cartService;
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AccountEntity accountEntity = accountRepository.findByEmail(email).orElseThrow(() ->
                new ObjectNotFoundException("Can't find account with email: {} in database.", email));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        accountEntity.getRoles().forEach(roleEntity -> authorities.add(new SimpleGrantedAuthority(roleEntity.getRole())));
        return new User(email, accountEntity.getPassword(), authorities);
    }

    @Override
    public Response<OnlyIdDTO> addNewAccount(AccountRequest request) {
        log.info("Saving new account {} to the database.", request.getEmail().substring(0, request.getEmail().indexOf("@")));
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmail(request.getEmail());
        accountEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        Long cartId = cartService.addNewCart().getData().getId();
        Long userId = userService.addNewUserDependOnCart(cartId).getData().getId();

        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("Can't find user with id {} in database.", userId));


        accountEntity.setUser(user);

        if (request.getRoles().isEmpty()){
            request.getRoles().add(USER);
        }
        accountEntity.setRoles(request.getRoles().stream().map(role -> roleRepository.findByRole(role).orElseThrow(
                () -> new ObjectNotFoundException("Can't find role with name: {} in database.", role)
        )).collect(Collectors.toSet()));
        accountRepository.save(accountEntity);
        return Response.<OnlyIdDTO>newBuilder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .id(accountEntity.getId())
                        .build())
                .build();
    }

    @Override
    public void addRoleToAccount(String email, String rolename) {
        log.info("Add role {} to user {} to the database.", rolename, email);
        AccountEntity accountEntity = accountRepository.findByEmail(email).orElseThrow(() ->
                new ObjectNotFoundException("Can't find account with email: ", email));
        RoleEntity roleEntity = roleRepository.findByRole(rolename).orElseThrow(() ->
                new ObjectNotFoundException("Can't  find role with name: ", rolename));
        accountEntity.getRoles().add(roleEntity);
        accountRepository.save(accountEntity);
    }

    @Override
    public Response<AccountDTO> getAccountByEmail(String email) {
        AccountEntity account = accountRepository.findByEmail(email).orElseThrow(() ->
                new ObjectNotFoundException("Can't find account with email: {}", email));
        return Response.<AccountDTO>newBuilder()
                .setSuccess(true)
                .setData(AccountDTO.newBuilder()
                        .setEmail(account.getEmail())
                        .setPassword(account.getPassword())
                        .setRoles(account.getRoles().stream().map(RoleEntity::getRole).collect(Collectors.toSet()))
                        .build())
                .build();
    }

}
