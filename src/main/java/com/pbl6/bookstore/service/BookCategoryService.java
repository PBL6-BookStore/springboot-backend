package com.pbl6.bookstore.service;

import com.pbl6.bookstore.payload.request.AddCategoryRequest;
import com.pbl6.bookstore.payload.response.ListDTO;
import com.pbl6.bookstore.payload.response.NoContentResponse;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.category.CategoryResponse;

/**
 * @author lkadai0801
 * @since 01/11/2022
 */
public interface BookCategoryService {
    Response<ListDTO<CategoryResponse>> findAll(String searchTerm);
    Response<OnlyIdDTO> addNewCategory(AddCategoryRequest request);
    Response<OnlyIdDTO> updateCategory(Long categoryId, AddCategoryRequest request);
    Response<NoContentResponse> deleteBookCategory(Long categoryId);
}
