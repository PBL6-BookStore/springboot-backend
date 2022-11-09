package com.pbl6.bookstore.service.impl;

import com.pbl6.bookstore.common.constant.BookStorePermission;
import com.pbl6.bookstore.common.enums.ErrorCode;
import com.pbl6.bookstore.domain.entity.AccountEntity;
import com.pbl6.bookstore.domain.entity.CartEntity;
import com.pbl6.bookstore.domain.entity.RoleEntity;
import com.pbl6.bookstore.domain.entity.UserEntity;
import com.pbl6.bookstore.domain.repository.jpa.AccountRepository;
import com.pbl6.bookstore.domain.repository.jpa.CartRepository;
import com.pbl6.bookstore.domain.repository.jpa.RoleRepository;
import com.pbl6.bookstore.domain.repository.jpa.UserRepository;
import com.pbl6.bookstore.exception.ObjectNotFoundException;
import com.pbl6.bookstore.payload.request.AccountRequest;
import com.pbl6.bookstore.payload.response.ErrorDTO;
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
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
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
    private static final String USER = BookStorePermission.Role.USER;

    private final CartRepository cartRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;
    private final UserService userService;
    private static final Pattern EMAIL_P = Pattern.compile("^[_A-Za-z0-9-']+(\\.['_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AccountEntity accountEntity = accountRepository.findByEmail(email).orElseThrow(() ->
                new ObjectNotFoundException("email", email));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        accountEntity.getRoles().forEach(roleEntity -> authorities.add(new SimpleGrantedAuthority(roleEntity.getRole())));
        return new User(email, accountEntity.getPassword(), authorities);
    }

    @Override
    @Transactional
    public Response<OnlyIdDTO> addNewAccount(AccountRequest request) {
        // validate email if exist
        if (!EMAIL_P.matcher(request.getEmail()).matches()){
            return Response.<OnlyIdDTO>newBuilder()
                    .setSuccess(false)
                    .setMessage("Bad request")
                    .setErrors(List.of(ErrorDTO.of("email", ErrorCode.EMAIL_INVALID)))
                    .setErrorCode(ErrorCode.EMAIL_INVALID)
                    .build();

        }

        if (accountRepository.findByEmail(request.getEmail()).isPresent()){
            return Response.<OnlyIdDTO>newBuilder()
                    .setSuccess(false)
                    .setErrorCode(ErrorCode.ALREADY_EXIST)
                    .setErrors(List.of(ErrorDTO.of("email", ErrorCode.ALREADY_EXIST)))
                    .setMessage("Bad request")
                    .build();
        }
        log.info("Saving new account {} to the database.", request.getEmail().substring(0, request.getEmail().indexOf("@")));
        AccountEntity accountEntity = new AccountEntity();


        accountEntity.setEmail(request.getEmail());
        accountEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        CartEntity cart = new CartEntity();
        UserEntity user = new UserEntity();
        user.setFirstName("");
        user.setLastName("");

        var roleUser = roleRepository.findByRole(USER).orElseThrow();
        accountEntity.setRoles(Set.of(roleUser));
        accountEntity.setUser(user);
        accountEntity.setCart(cart);
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
                new ObjectNotFoundException("email", email));
        RoleEntity roleEntity = roleRepository.findByRole(rolename).orElseThrow(() ->
                new ObjectNotFoundException("rolename", rolename));
        accountEntity.getRoles().add(roleEntity);
        accountRepository.save(accountEntity);
    }

    @Override
    public Response<AccountDTO> getAccountByEmail(String email) {
        AccountEntity account = accountRepository.findByEmail(email).orElseThrow(() ->
                new ObjectNotFoundException("email", email));
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
