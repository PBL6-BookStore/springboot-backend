package com.pbl6.bookstore.service;

import com.pbl6.bookstore.payload.request.AddBookToCartRequest;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.cart.ListCartDetailDTO;

/**
 * @author lkadai0801
 * @since 02/11/2022
 */
public interface CartService {
    Response<OnlyIdDTO> addNewCart();
    Response<OnlyIdDTO> addBookToCart(AddBookToCartRequest request);
    Response<ListCartDetailDTO> getCart(Long cartId);
}
