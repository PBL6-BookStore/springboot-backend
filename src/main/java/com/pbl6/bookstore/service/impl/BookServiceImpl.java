package com.pbl6.bookstore.service.impl;

import com.pbl6.bookstore.common.enums.ErrorCode;
import com.pbl6.bookstore.domain.entity.BookEntity;
import com.pbl6.bookstore.domain.repository.dsl.BookDslRepository;
import com.pbl6.bookstore.domain.repository.jpa.BookCategoryRepository;
import com.pbl6.bookstore.domain.repository.jpa.BookRepository;
import com.pbl6.bookstore.exception.ObjectNotFoundException;
import com.pbl6.bookstore.exception.ValidateException;
import com.pbl6.bookstore.payload.request.BookRequest;
import com.pbl6.bookstore.payload.request.ListBookRequest;
import com.pbl6.bookstore.payload.response.ErrorDTO;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.PageDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.book.BookDTO;
import com.pbl6.bookstore.service.BookService;
import static com.pbl6.bookstore.util.RequestUtils.*;

import com.pbl6.bookstore.service.converter.BookMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lkadai0801
 * @since 29/10/2022
 */

@RequiredArgsConstructor
@Service
@Log4j2
public class BookServiceImpl implements BookService {
    private final BookDslRepository bookDslRepository;

    private final BookRepository bookRepository;

    private final BookCategoryRepository bookCategoryRepository;

    private final String pattern = "dd-MM-yyyy";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    @Override
    public Response<PageDTO<BookDTO>> listBook(ListBookRequest request) {
        var listBookResponse = bookDslRepository.getListBook(request);
        return Response.<PageDTO<BookDTO>>newBuilder()
                .setSuccess(true)
                .setData(PageDTO.<BookDTO>newBuilder()
                        .setPage(request.getPage())
                        .setSize(request.getSize())
                        .setTotalPages(getTotalPage(listBookResponse.getTotal(), request))
                        .setTotalElements(listBookResponse.getTotal())
                        .setItems(listBookResponse.getItems().stream()
                                .map(BookMapper::map)
                                .collect(Collectors.toUnmodifiableList()))
                        .build())
                .build();
    }

    @Override
    public Response<BookDTO> findBookById(Long bookId) {
        var book = bookRepository.findById(bookId).orElseThrow(() ->
                new ObjectNotFoundException("bookId", bookId));
        return Response.<BookDTO>newBuilder()
                .setSuccess(true)
                .setData(BookMapper.map(book))
                .build();
    }

    @Override
    public Response<OnlyIdDTO> addNewBook(BookRequest request) {

        // validate
        List<ErrorDTO> errors = new ArrayList<>();

        if (request.getCategoryId() == null){
            errors.add(ErrorDTO.of("categoryId", ErrorCode.REQUIRED_FIELD_MISSING));
        } else {
            var bookCategory = bookCategoryRepository.findById(request.getCategoryId());

            if (bookCategory.isEmpty()){
                errors.add(ErrorDTO.of("categoryId", ErrorCode.RESOURCE_NOT_FOUND));
            }
        }

        if (request.getTitle() == null){
            errors.add(ErrorDTO.of("title", ErrorCode.REQUIRED_FIELD_MISSING));
        } else if (!StringUtils.hasText(request.getTitle())){
            errors.add(ErrorDTO.of("title", ErrorCode.NOT_EMPTY));
        }

        Timestamp date = null;
        if (StringUtils.hasText(request.getPublicationDate())){
            try {
                date = new Timestamp(simpleDateFormat.parse(request.getPublicationDate()).getTime());
            } catch (ParseException e) {
                log.warn("Date format invalid {}", request.getPublicationDate());
                errors.add(ErrorDTO.of("publicationDate", ErrorCode.DATE_FORMAT_INVALID));
            }
        }

        // end validate

        if (!errors.isEmpty()){
            throw new ValidateException(errors);
        }

        BookEntity newBook = new BookEntity();
        newBook.setTitle(request.getTitle());
        newBook.setAuthor(request.getAuthor());
        newBook.setEdition(request.getEdition());
        newBook.setPublicationDate(date);
        newBook.setSize(request.getSize());
        newBook.setDescription(request.getDescription());
        newBook.setPages(request.getPages());
        newBook.setPrice(request.getPrice());
        newBook.setWeight(request.getWeight());
        newBook.setPublisher(request.getPublisher());

        var bookSaved = bookRepository.save(newBook);
        return Response.<OnlyIdDTO>newBuilder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .id(bookSaved.getId())
                        .build())
                .build();

    }
}
