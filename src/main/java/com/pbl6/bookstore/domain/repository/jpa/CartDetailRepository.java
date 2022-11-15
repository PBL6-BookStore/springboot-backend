package com.pbl6.bookstore.domain.repository.jpa;

import com.pbl6.bookstore.domain.entity.CartDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lkadai0801
 * @since 04/11/2022
 */

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetailEntity, Long> {
    List<CartDetailEntity> findAllByCartId(Long cartId);

    @Query("SELECT cd FROM CartDetailEntity cd " +
            "LEFT JOIN FETCH cd.book " +
            "WHERE cd.cart.id = :cartId")
    List<CartDetailEntity> findAllByCartIdFetchBook(@Param("cartId") Long cartId);

    @Query("SELECT cd FROM CartDetailEntity cd " +
            "LEFT JOIN FETCH cd.book " +
            "WHERE cd.id IN (:cartDetailIds) " +
            "AND cd.cart.id = :cartId")
    List<CartDetailEntity> findAllByCartDetailIdsFetchBook(@Param("cartDetailIds") List<Long> cartDetailIds, @Param("cartId") Long cartId);
}
