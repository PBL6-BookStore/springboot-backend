package com.pbl6.bookstore.test;

import com.pbl6.bookstore.domain.entity.AccountEntity;
import com.pbl6.bookstore.domain.entity.CartEntity;
import com.pbl6.bookstore.domain.entity.RoleEntity;
import com.pbl6.bookstore.domain.entity.UserEntity;
import com.pbl6.bookstore.domain.repository.jpa.AccountRepository;
import com.pbl6.bookstore.domain.repository.jpa.CartRepository;
import com.pbl6.bookstore.domain.repository.jpa.RoleRepository;
import com.pbl6.bookstore.domain.repository.jpa.UserRepository;
import com.pbl6.bookstore.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

/**
 * @author vndat00
 * @since 05/11/2022
 */

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private RoleRepository roleRepository;


    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void createAccountTest(){
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setEmail("guest@gmail.com");
        accountEntity.setPassword(passwordEncoder.encode("guest"));

        CartEntity cart = new CartEntity();
        cartRepository.save(cart);

        UserEntity user = new UserEntity();
        user.setFirstName("");
        user.setLastName("");
        user.setCart(cart);
        userRepository.save(user);

        accountEntity.setUser(user);

        accountRepository.save(accountEntity);
    }

    @Test
    public void addRoleToAccount(){
        AccountEntity accountEntity = accountRepository.findByEmail("guest@gmail.com").orElseThrow(() ->
                new ObjectNotFoundException("khong tim dc tai khoan {}", "guest@gmail.com"));
        RoleEntity roleEntity = roleRepository.findByRole("ROLE_USER").orElseThrow(() ->
                ( new ObjectNotFoundException("Khong co role {}", "ROLE_USER")));
        accountEntity.getRoles().add(roleEntity);
        accountRepository.save(accountEntity);
    }
}
