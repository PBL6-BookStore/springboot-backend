package com.pbl6.bookstore.service.impl;

import com.pbl6.bookstore.common.enums.ErrorCode;
import com.pbl6.bookstore.domain.entity.BookCategoryEntity;
import com.pbl6.bookstore.domain.repository.dsl.BookCategoryDslRepository;
import com.pbl6.bookstore.domain.repository.jpa.BookCategoryRepository;
import com.pbl6.bookstore.exception.ObjectNotFoundException;
import com.pbl6.bookstore.payload.request.AddCategoryRequest;
import com.pbl6.bookstore.payload.response.*;
import com.pbl6.bookstore.payload.response.category.CategoryResponse;
import com.pbl6.bookstore.service.BookCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lkadai0801
 * @since 01/11/2022
 */

@Service
@RequiredArgsConstructor
public class BookCategoryServiceImpl implements BookCategoryService {
    private final BookCategoryDslRepository bookCategoryDslRepository;
    private final BookCategoryRepository bookCategoryRepository;
    @Override
    public Response<ListDTO<CategoryResponse>> findAll(String searchTerm) {
        var categories = bookCategoryDslRepository.findAll(searchTerm);
        return Response.<ListDTO<CategoryResponse>>newBuilder()
                .setSuccess(true)
                .setData(ListDTO.<CategoryResponse>newBuilder()
                        .setTotalElements(categories.getTotal())
                        .setItems(categories.getItems().stream()
                                .map(c -> CategoryResponse.newBuilder()
                                        .setId(c.getId())
                                        .setName(c.getName())
                                        .build())
                                .collect(Collectors.toUnmodifiableList()))
                        .build())
                .build();
    }

    @Override
    public Response<OnlyIdDTO> addNewCategory(AddCategoryRequest request) {
        if (!StringUtils.hasText(request.getName())){
            return Response.<OnlyIdDTO>newBuilder()
                    .setSuccess(false)
                    .setMessage("Bad request")
                    .setErrorCode(ErrorCode.REQUIRED_FIELD_MISSING)
                    .setErrors(List.of(ErrorDTO.of("name", ErrorCode.REQUIRED_FIELD_MISSING)))
                    .build();
        }
        BookCategoryEntity bookCategory = new BookCategoryEntity();
        bookCategory.setName(request.getName());
        var bookCategorySaved = bookCategoryRepository.save(bookCategory);
        return Response.<OnlyIdDTO>newBuilder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .id(bookCategorySaved.getId())
                        .build())
                .build();
    }

    @Override
    public Response<OnlyIdDTO> updateCategory(Long categoryId, AddCategoryRequest request) {
        var category = bookCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("categoryId", categoryId));
        if (!StringUtils.hasText(request.getName())){
            return Response.<OnlyIdDTO>newBuilder()
                    .setSuccess(false)
                    .setErrors(List.of(ErrorDTO.of("name", ErrorCode.REQUIRED_FIELD_MISSING)))
                    .setMessage("Bad request")
                    .setErrorCode(ErrorCode.REQUIRED_FIELD_MISSING)
                    .build();
        }

        category.setName(request.getName());
        var categorySaved = bookCategoryRepository.save(category);
        return Response.<OnlyIdDTO>newBuilder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .id(categorySaved.getId())
                        .build())
                .build();
    }

    @Override
    public Response<NoContentResponse> deleteBookCategory(Long categoryId) {
        var category = bookCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ObjectNotFoundException("categoryId", categoryId));
        bookCategoryRepository.delete(category);
        return Response.<NoContentResponse>newBuilder()
                .setSuccess(true)
                .setData(NoContentResponse.builder().build())
                .build();
    }
}
