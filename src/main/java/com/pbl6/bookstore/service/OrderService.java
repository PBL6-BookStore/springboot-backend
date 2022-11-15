package com.pbl6.bookstore.service;

import com.pbl6.bookstore.payload.request.NewOrderRequest;
import com.pbl6.bookstore.payload.request.PreOrderRequest;
import com.pbl6.bookstore.payload.response.ListDTO;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.order.OrderDTO;

/**
 * @author lkadai0801
 * @since 05/11/2022
 */
public interface OrderService {
    Response<OnlyIdDTO> addNewOrder(NewOrderRequest request);
    Response<OrderDTO> getPreOder(PreOrderRequest request);
}
