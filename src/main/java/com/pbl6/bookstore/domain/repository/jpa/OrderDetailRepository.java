package com.pbl6.bookstore.domain.repository.jpa;

import com.pbl6.bookstore.domain.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lkadai0801
 * @since 05/11/2022
 */

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
}
