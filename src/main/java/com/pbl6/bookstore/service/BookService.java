package com.pbl6.bookstore.service;

import com.pbl6.bookstore.payload.request.ListBookRequest;
import com.pbl6.bookstore.payload.response.PageDTO;
import com.pbl6.bookstore.payload.response.book.BookDTO;

/**
 * @author lkadai0801
 * @since 29/10/2022
 */
public interface BookService {
    PageDTO<BookDTO> listBook(ListBookRequest request);
}
