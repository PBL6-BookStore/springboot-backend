package com.pbl6.bookstore.service.converter;

import com.pbl6.bookstore.domain.entity.BookEntity;
import com.pbl6.bookstore.payload.response.book.BookDTO;
import com.pbl6.bookstore.util.DateTimeUtils;

import java.text.SimpleDateFormat;

import static com.pbl6.bookstore.util.RequestUtils.blankIfNull;
import static com.pbl6.bookstore.util.RequestUtils.defaultIfNull;

/**
 * @author lkadai0801
 * @since 29/10/2022
 */
public class BookMapper {
    public static BookDTO map(BookEntity book){

        var bookBuilder =  BookDTO.newBuilder()
                .setAuthor(blankIfNull(book.getAuthor()))
                .setPrice(book.getPrice())
                .setImage(blankIfNull(book.getImage()))
                .setTitle(book.getTitle())
                .setId(book.getId())
                .setPublisher(book.getPublisher())
                .setEdition(defaultIfNull(book.getEdition(), 0))
                .setDescription(blankIfNull(book.getDescription()));
        if (book.getPublicationDate() != null){
            bookBuilder.setPublicationDate(DateTimeUtils.timestamp2String(book.getPublicationDate()));
        }
        return bookBuilder.build();
    }
}
