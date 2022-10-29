package com.pbl6.bookstore.service.impl;

import com.pbl6.bookstore.domain.repository.dsl.BookDslRepository;
import com.pbl6.bookstore.payload.request.ListBookRequest;
import com.pbl6.bookstore.payload.response.PageDTO;
import com.pbl6.bookstore.payload.response.book.BookDTO;
import com.pbl6.bookstore.service.BookService;
import static com.pbl6.bookstore.util.RequestUtils.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

/**
 * @author lkadai0801
 * @since 29/10/2022
 */

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookDslRepository bookDslRepository;

    private final String pattern = "dd-MM-yyyy";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    @Override
    public PageDTO<BookDTO> listBook(ListBookRequest request) {
        var listBookResponse = bookDslRepository.getListBook(request);
        return PageDTO.<BookDTO>newBuilder()
                .setPage(request.getPage())
                .setSize(request.getSize())
                .setTotalPages(getTotalPage(listBookResponse.getTotal(), request))
                .setTotalElements(listBookResponse.getTotal())
                .setItems(listBookResponse.getItems().stream()
                        .map(b -> BookDTO.newBuilder()
                                .setAuthor(blankIfNull(b.getAuthor()))
                                .setPrice(b.getPrice())
                                .setImage(blankIfNull(b.getImage()))
                                .setTitle(b.getTitle())
                                .setId(b.getId())
                                .setPublisher(b.getPublisher())
                                .setEdition(defaultIfNull(b.getEdition(), 0))
                                .setPublicationDate(simpleDateFormat.format(b.getPublicationDate()))
                                .build())
                        .collect(Collectors.toUnmodifiableList()))
                .build();
    }
}
