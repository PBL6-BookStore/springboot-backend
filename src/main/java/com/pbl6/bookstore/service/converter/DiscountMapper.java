package com.pbl6.bookstore.service.converter;

import com.pbl6.bookstore.domain.entity.DiscountEntity;
import com.pbl6.bookstore.payload.response.discount.DiscountDTO;
import com.pbl6.bookstore.util.DateTimeUtils;

/**
 * @author lkadai0801
 * @since 12/11/2022
 */
public class DiscountMapper {
    public static DiscountDTO map(DiscountEntity discount){
        return DiscountDTO.newBuilder()
                .setId(discount.getId())
                .setName(discount.getName())
                .setCode(discount.getCode())
                .setDescription(discount.getDescription())
                .setStartDate(DateTimeUtils.timestamp2String(discount.getStartDate()))
                .setEndDate(DateTimeUtils.timestamp2String(discount.getEndDate()))
                .setValue(discount.getValue())
                .build();
    }
}
