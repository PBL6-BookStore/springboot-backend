package com.pbl6.bookstore.service;

import com.pbl6.bookstore.payload.request.NewOrderRequest;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;

/**
 * @author lkadai0801
 * @since 05/11/2022
 */
public interface OrderService {
    Response<OnlyIdDTO> addNewOrder(NewOrderRequest request);
}
