package com.pbl6.bookstore.domain.repository.jpa;

import com.pbl6.bookstore.domain.entity.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lkadai0801
 * @since 05/11/2022
 */

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, Long> {
    @Query("SELECT d FROM DiscountEntity d " +
            "WHERE (:isActive = TRUE AND date(d.startDate) <= date(current_date) AND date(current_date ) <= date(d.endDate)) " +
            "OR :isActive = FALSE")
    List<DiscountEntity> findAllDiscount(@Param("isActive") Boolean isActive);
}
