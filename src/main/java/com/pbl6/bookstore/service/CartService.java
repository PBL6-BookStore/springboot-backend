package com.pbl6.bookstore.service;

import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;

/**
 * @author lkadai0801
 * @since 02/11/2022
 */
public interface CartService {
    Response<OnlyIdDTO> addNewCart();
}
