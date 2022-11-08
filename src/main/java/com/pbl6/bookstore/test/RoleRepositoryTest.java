package com.pbl6.bookstore.test;

import com.pbl6.bookstore.domain.entity.RoleEntity;
import com.pbl6.bookstore.domain.repository.jpa.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

/**
 * @author vndat00
 * @since 05/11/2022
 */

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;
    @Test
    public void testCreateRoles(){
        RoleEntity admin = new RoleEntity("ROLE_ADMIN","Role admin");
        RoleEntity user = new RoleEntity("ROLE_USER", "Role user");

        roleRepository.saveAll(List.of(admin, user));
    }
}
