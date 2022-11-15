package com.pbl6.bookstore.domain.repository.jpa;

import com.pbl6.bookstore.domain.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author lkadai0801
 * @since 02/11/2022
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByEmail(String email);

    @Query("SELECT a FROM AccountEntity a " +
            "LEFT JOIN FETCH a.user " +
            "WHERE  a.email = :email")
    Optional<AccountEntity> findByEmailFetchUser(@Param("email") String email);
}
