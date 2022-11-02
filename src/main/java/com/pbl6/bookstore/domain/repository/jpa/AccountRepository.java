package com.pbl6.bookstore.domain.repository.jpa;

import com.pbl6.bookstore.domain.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lkadai0801
 * @since 02/11/2022
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
