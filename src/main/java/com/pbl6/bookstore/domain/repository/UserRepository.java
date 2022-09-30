package com.pbl6.bookstore.domain.repository;

import com.pbl6.bookstore.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lkadai0801
 * @since 30/09/2022
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
