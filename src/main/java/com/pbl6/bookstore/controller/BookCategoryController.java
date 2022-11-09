package com.pbl6.bookstore.controller;

import com.pbl6.bookstore.common.constant.BookStorePermission;
import com.pbl6.bookstore.payload.request.AddCategoryRequest;
import com.pbl6.bookstore.payload.response.ListDTO;
import com.pbl6.bookstore.payload.response.NoContentResponse;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.category.CategoryResponse;
import com.pbl6.bookstore.service.BookCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * @author lkadai0801
 * @since 01/11/2022
 */

@Tag(name = "Category", description = "Category APIs")
@RestController
@RequiredArgsConstructor
public class BookCategoryController {

    private final BookCategoryService bookCategoryService;

    @Operation(summary = "Get all category", description = "Get all category for book")
    @Parameter(in = ParameterIn.QUERY, name = "searchTerm", description = "Search category. Not apply if not given", example = "humor")
    @GetMapping("/anonymous/categories")
    public Response<ListDTO<CategoryResponse>> getAllCategory(@RequestParam(value = "searchTerm", required = false) String searchTerm){
        return bookCategoryService.findAll(searchTerm);
    }


    @Secured({
            BookStorePermission.Role.ADMIN
    })
    @Operation(summary = "Add new Category", description = "Add new category for book store")
    @PostMapping("/categories")
    public Response<OnlyIdDTO> addNewCategory(@RequestBody AddCategoryRequest request){
        return bookCategoryService.addNewCategory(request);
    }

    @Secured({
            BookStorePermission.Role.ADMIN
    })
    @Operation(summary = "Update category", description = "Update category for book store")
    @PutMapping("/categories/{categoryId}")
    public Response<OnlyIdDTO> updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody AddCategoryRequest request){
        return bookCategoryService.updateCategory(categoryId, request);
    }

    @Secured({
            BookStorePermission.Role.ADMIN
    })
    @Operation(summary = "Delete category", description = "Delete category for book store")
    @DeleteMapping("/categories/{categoryId}")
    public Response<NoContentResponse> deleteBookCategory(@PathVariable("categoryId") Long categoryId){
        return bookCategoryService.deleteBookCategory(categoryId);
    }
}
