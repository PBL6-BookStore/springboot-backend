package com.pbl6.bookstore.controller;

import com.pbl6.bookstore.payload.request.BookRequest;
import com.pbl6.bookstore.payload.request.ListBookRequest;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.PageDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.book.BookDTO;
import com.pbl6.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author lkadai0801
 * @since 29/10/2022
 */

@Tag(name = "Book", description = "Book APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Operation(summary = "List Books", description = "List book with pagination and sorting supported")
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "page", description = "Request page. Start from 1. Default 1 if not given", example = "1", schema = @Schema(minimum = "1")),
            @Parameter(in = ParameterIn.QUERY, name = "size", description = "Request page size. Default 100 if not given",
            example = "100", schema = @Schema(minimum = "1")),
            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "Sort property. Omitted if not given", example = "title",
            schema = @Schema(allowableValues = {"title", "category", "author"})),
            @Parameter(in = ParameterIn.QUERY, name = "direction", description = "Sort direction. Omitted if not given", example = "ASC",
            schema = @Schema(allowableValues = {"ASC", "DESC"})),
            @Parameter(in = ParameterIn.QUERY, name = "searchTerm", description = "Search book with title or author. Not apply if not given", example = "Khoa hoc"),
            @Parameter(in = ParameterIn.QUERY, name = "categoryId", description = "Filter with category id. Not apply if not given", example = "5"),
            @Parameter(in = ParameterIn.QUERY, name = "allInOne", description = "Fetch all book in database. Default false", example = "true",
            schema = @Schema(allowableValues = {"true", "false"}))
    })
    @GetMapping
    public Response<PageDTO<BookDTO>> listBook(@ModelAttribute ListBookRequest request){
        var listBook = bookService.listBook(request);
        return bookService.listBook(request);
    }

    @Operation(summary = "Find Book by id", description = "Find book by id")
    @GetMapping("/{bookId}")
    public Response<BookDTO> getBookById(@PathVariable("bookId") Long bookId){
        var bookRes = bookService.findBookById(bookId);
        return bookService.findBookById(bookId);
    }

    @Operation(summary = "Add new Book", description = "Add new Book")
    @PostMapping
    public Response<OnlyIdDTO> addBook(@RequestBody BookRequest request){
        return bookService.addNewBook(request);
    }
}