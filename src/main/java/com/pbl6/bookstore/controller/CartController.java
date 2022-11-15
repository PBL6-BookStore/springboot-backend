package com.pbl6.bookstore.controller;

import com.pbl6.bookstore.payload.request.AddBookToCartRequest;
import com.pbl6.bookstore.payload.response.NoContentResponse;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.cart.ListCartDetailDTO;
import com.pbl6.bookstore.service.CartService;
import com.pbl6.bookstore.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lkadai0801
 * @since 12/11/2022
 */

@Tag(name = "Cart", description = "Cart APIs")
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final SecurityUtils securityUtils;

    @Operation(summary = "Add book to cart", description = "Add book to cart api")
    @PostMapping("/carts/books")
    public Response<OnlyIdDTO> addBookToCart(@RequestBody AddBookToCartRequest request){
        var principal = securityUtils.getPrincipal();
        return cartService.addBookToCart(AddBookToCartRequest.newBuilder()
                        .setCartId(principal.getCartId())
                        .setBookId(request.getBookId())
                        .setQuantity(request.getQuantity())
                .build());
    }

    @Operation(summary = "Get my cart", description = "Get my cart api")
    @GetMapping("/carts")
    public Response<ListCartDetailDTO> getCart(){
        var principal = securityUtils.getPrincipal();
        return cartService.getCart(principal.getCartId());
    }

    @Operation(summary = "Delete cart detail", description = "Delete cart detail")
    @GetMapping("/carts/cart-details")
    public Response<NoContentResponse> deleteCartDetail(@RequestParam("cartDetailIds")List<Long> cartDetailIds){
        return cartService.deleteCartDetail(cartDetailIds);
    }
}
