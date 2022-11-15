package com.pbl6.bookstore.controller;

import com.pbl6.bookstore.common.constant.BookStorePermission;
import com.pbl6.bookstore.payload.request.AddDiscountRequest;
import com.pbl6.bookstore.payload.request.GetAllDiscountRequest;
import com.pbl6.bookstore.payload.response.ListDTO;
import com.pbl6.bookstore.payload.response.NoContentResponse;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.discount.DiscountDTO;
import com.pbl6.bookstore.service.DiscountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * @author lkadai0801
 * @since 15/11/2022
 */

@RestController
@RequiredArgsConstructor
@Tag(name = "Discount", description = "Discount APIs")
public class DiscountController {
    private final DiscountService discountService;

    @Operation(summary = "Get all discount", description = "Get all discount in DB")
    @Parameter(in = ParameterIn.QUERY, name = "isActive", description = "Get active discount. Not apply if not given", example = "true",
            schema = @Schema(allowableValues = {"true", "false"}))
    @Parameter(in = ParameterIn.QUERY, name = "code", description = "Find with code. Required with user", example = "HAPPY_HOURS")
    @Secured({
            BookStorePermission.Role.ADMIN,
            BookStorePermission.Role.USER
    })
    @GetMapping("/discounts")
    public Response<ListDTO<DiscountDTO>> getAllDiscount(@ModelAttribute GetAllDiscountRequest request){
        return discountService.getAllDiscount(request);
    }

    @Operation(summary = "Get discount by id", description = "Get discount with id in DB")
    @Secured({
            BookStorePermission.Role.ADMIN
    })
    @GetMapping("/discounts/{discountId}")
    public Response<DiscountDTO> getDiscountById(@PathVariable("discountId") Long discountId){
        return discountService.getDiscountById(discountId);
    }

    @Operation(summary = "Add new discount", description = "Add new discount")
    @Secured({
            BookStorePermission.Role.ADMIN
    })
    @PostMapping("/discounts")
    public Response<OnlyIdDTO> addDiscount(@RequestBody AddDiscountRequest request){
        return discountService.addDiscount(request);
    }


    @Operation(summary = "Update discount")
    @Secured({
            BookStorePermission.Role.ADMIN
    })
    @PutMapping("/discounts/{discountId}")
    public Response<OnlyIdDTO> updateDiscount(@RequestBody AddDiscountRequest request, @PathVariable("discountId") Long discountId){
        return discountService.updateDiscount(request, discountId);
    }

    @Operation(summary = "Delete discount")
    @Secured({
            BookStorePermission.Role.ADMIN
    })
    @DeleteMapping("/discounts/{discountId}")
    public Response<NoContentResponse> deleteDiscount(@PathVariable("discountId") Long discountId){
        return discountService.deleteDiscount(discountId);
    }
}
