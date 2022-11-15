package com.pbl6.bookstore.service.impl;

import com.pbl6.bookstore.common.constant.Constant;
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
import com.pbl6.bookstore.util.DateTimeUtils;
import com.pbl6.bookstore.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
        Timestamp date = null;

        validateBook(errors, request);


        // end validate
        if (!errors.isEmpty()){
            throw new ValidateException(errors);
        }

        if (StringUtils.hasText(request.getPublicationDate())){
            try {
                date = DateTimeUtils.string2Timestamp(request.getPublicationDate());
            } catch (ParseException e){
                log.error("date format");
            }
        }

        BookEntity book = new BookEntity();
        setBookField(book, request, date);

        var bookSaved = bookRepository.save(book);
        return Response.<OnlyIdDTO>newBuilder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .id(bookSaved.getId())
                        .build())
                .build();

    }

    @Override
    @Transactional
    public Response<OnlyIdDTO> updateImage(Long bookId, MultipartFile image) throws IOException {
        var book = bookRepository.findById(bookId).orElseThrow(() ->
                new ObjectNotFoundException("bookId", bookId));
        if (!StringUtils.hasText(image.getOriginalFilename())){
            return Response.<OnlyIdDTO>newBuilder()
                    .setSuccess(false)
                    .setMessage("Bad request")
                    .setErrorCode(ErrorCode.REQUIRED_FIELD_MISSING)
                    .setErrors(List.of(ErrorDTO.of("image", ErrorCode.REQUIRED_FIELD_MISSING)))
                    .build();
        }
        String fileName1 = StringUtils.cleanPath(image.getOriginalFilename());
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + fileName1.substring(fileName1.lastIndexOf("."));
        book.setImage(Constant.staticImageUri + book.getId() + "/" + fileName);
        String uploadDir = Constant.staticUri + Constant.staticImageUri + book.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, image);
        bookRepository.save(book);
        return Response.<OnlyIdDTO>newBuilder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .id(bookId)
                        .build())
                .build();
    }

    @Override
    public Response<OnlyIdDTO> updateBook(Long bookId, BookRequest request) {
        var book = bookRepository.findById(bookId).orElseThrow(() ->
                new ObjectNotFoundException("bookId", bookId));
        
        List<ErrorDTO> errors = new ArrayList<>();
        Timestamp date = null;
        
        validateBook(errors, request);

        if (!errors.isEmpty()){
            throw new ValidateException(errors);
        }

        if (StringUtils.hasText(request.getPublicationDate())){
            try {
                date = DateTimeUtils.string2Timestamp(request.getPublicationDate());
            } catch (ParseException e){
                log.error("date format");
            }
        }

        setBookField(book, request, date);

        var bookSaved = bookRepository.save(book);
        return Response.<OnlyIdDTO>newBuilder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .id(bookSaved.getId())
                        .build())
                .build();
    }

    @Override
    public Response<OnlyIdDTO> deleteBook(Long bookId) {
        var book = bookRepository.findById(bookId).orElseThrow(() ->
                new ObjectNotFoundException("bookId", bookId));
        bookRepository.delete(book);
        return Response.<OnlyIdDTO>newBuilder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .id(bookId)
                        .build())
                .build();
    }

    private void validateBook(List<ErrorDTO> errors, BookRequest request){
        // validate

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

        if (StringUtils.hasText(request.getPublicationDate())){
            try {
                DateTimeUtils.string2Timestamp(request.getPublicationDate());
            } catch (ParseException e) {
                log.warn("Date format invalid {}", request.getPublicationDate());
                errors.add(ErrorDTO.of("publicationDate", ErrorCode.DATE_FORMAT_INVALID));
            }
        }
        // end validate
    }

    private BookEntity setBookField(BookEntity book, BookRequest request, Timestamp date){
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setEdition(request.getEdition());
        book.setPublicationDate(date);
        book.setSize(request.getSize());
        book.setDescription(request.getDescription());
        book.setPages(request.getPages());
        book.setPrice(request.getPrice());
        book.setWeight(request.getWeight());
        book.setPublisher(request.getPublisher());
        var category = bookCategoryRepository.findById(request.getCategoryId()).orElseThrow(
                () -> new ObjectNotFoundException("categoryId", request.getCategoryId())
        );
        book.setBookCategory(category);
        return book;
    }

}
