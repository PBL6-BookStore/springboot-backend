package com.pbl6.bookstore.domain.repository.jpa;

import com.pbl6.bookstore.domain.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author lkadai0801
 * @since 02/11/2022
 */

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRole(String role);
}
