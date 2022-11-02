package com.pbl6.bookstore.domain.repository.jpa;

import com.pbl6.bookstore.domain.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author lkadai0801
 * @since 02/11/2022
 */
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
