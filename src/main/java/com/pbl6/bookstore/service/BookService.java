package com.pbl6.bookstore.service;

import com.pbl6.bookstore.payload.request.BookRequest;
import com.pbl6.bookstore.payload.request.ListBookRequest;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.PageDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.book.BookDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author lkadai0801
 * @since 29/10/2022
 */
public interface BookService {
    Response<PageDTO<BookDTO>> listBook(ListBookRequest request);
    Response<BookDTO> findBookById(Long bookId);
    Response<OnlyIdDTO> addNewBook(BookRequest request);
    Response<OnlyIdDTO> updateImage(Long bookId, MultipartFile image) throws IOException;
    Response<OnlyIdDTO> updateBook(Long bookId, BookRequest request);

    Response<OnlyIdDTO> deleteBook(Long bookId);
}
