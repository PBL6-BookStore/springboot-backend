package com.pbl6.bookstore.controller;

import com.pbl6.bookstore.payload.request.NewOrderRequest;
import com.pbl6.bookstore.payload.request.PreOrderRequest;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.order.OrderDTO;
import com.pbl6.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author lkadai0801
 * @since 15/11/2022
 */

@RestController
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order APIs")
public class OrderController {
    private final OrderService orderService;


    @Operation(summary = "Get pre order", description = "Get pre order")
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "cartDetailId", description = "List cart detail id", required = true),
            @Parameter(in = ParameterIn.QUERY, name = "discountId", description = "Discount id apply, not apply if not given")
    })
    @GetMapping("/orders/pre")
    public Response<OrderDTO> getPreOrder(@ModelAttribute PreOrderRequest request){
        return orderService.getPreOder(request);
    }


    @Operation(summary = "Add new order", description = "Add new order")
    @PostMapping("/orders")
    public Response<OnlyIdDTO> addNewOrder(@RequestBody NewOrderRequest request){
        return orderService.addNewOrder(request);
    }
}
