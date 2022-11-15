package com.pbl6.bookstore.service;

import com.pbl6.bookstore.payload.request.AddDiscountRequest;
import com.pbl6.bookstore.payload.request.GetAllDiscountRequest;
import com.pbl6.bookstore.payload.response.ListDTO;
import com.pbl6.bookstore.payload.response.NoContentResponse;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.discount.DiscountDTO;

/**
 * @author lkadai0801
 * @since 12/11/2022
 */
public interface DiscountService {
    Response<ListDTO<DiscountDTO>> getAllDiscount(GetAllDiscountRequest request);

    Response<DiscountDTO> getDiscountById(Long discountId);

    Response<OnlyIdDTO> addDiscount(AddDiscountRequest request);

    Response<OnlyIdDTO> updateDiscount(AddDiscountRequest request, Long discountId);

    Response<NoContentResponse> deleteDiscount(Long discountId);
}
