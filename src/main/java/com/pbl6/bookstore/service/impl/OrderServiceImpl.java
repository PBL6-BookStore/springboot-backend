package com.pbl6.bookstore.service.impl;

import com.pbl6.bookstore.common.enums.ErrorCode;
import com.pbl6.bookstore.common.enums.OrderStatus;
import com.pbl6.bookstore.domain.entity.DiscountEntity;
import com.pbl6.bookstore.domain.entity.OrderDetailEntity;
import com.pbl6.bookstore.domain.entity.OrderEntity;
import com.pbl6.bookstore.domain.prefetch.PrefetchEntityProvider;
import com.pbl6.bookstore.domain.repository.jpa.*;
import com.pbl6.bookstore.exception.ObjectNotFoundException;
import com.pbl6.bookstore.payload.request.NewOrderRequest;
import com.pbl6.bookstore.payload.request.PreOrderRequest;
import com.pbl6.bookstore.payload.response.ErrorDTO;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.discount.DiscountDTO;
import com.pbl6.bookstore.payload.response.order.OrderDTO;
import com.pbl6.bookstore.service.OrderService;
import com.pbl6.bookstore.util.DateTimeUtils;
import com.pbl6.bookstore.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lkadai0801
 * @since 05/11/2022
 */

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartDetailRepository cartDetailRepository;
    private final DiscountRepository discountRepository;
    private final AccountRepository accountRepository;
    private final PrefetchEntityProvider prefetchEntityProvider;
    private final SecurityUtils securityUtils;

    @Override
    public Response<OnlyIdDTO> addNewOrder(NewOrderRequest request) {
        if (request.getCartDetailIds() == null || request.getCartDetailIds().isEmpty()){
            return Response.<OnlyIdDTO>newBuilder()
                    .setSuccess(false)
                    .setMessage("Bad request")
                    .setErrorCode(ErrorCode.NOT_EMPTY)
                    .setErrors(List.of(ErrorDTO.of("cartDetailIds", ErrorCode.NOT_EMPTY)))
                    .build();
        }

        DiscountEntity discount = null;

        if (request.getDiscountId() != null && request.getDiscountId() > 0){
            discount = discountRepository.findById(request.getDiscountId()).orElseThrow(
                    () -> new ObjectNotFoundException("discountId", request.getDiscountId())
            );
            Date now = new Date();
            if (discount.getStartDate().compareTo(now) > 0 || discount.getEndDate().compareTo(now) < 0){
                return Response.<OnlyIdDTO>newBuilder()
                        .setSuccess(false)
                        .setMessage("Discount is expired!")
                        .setErrorCode(ErrorCode.DISCOUNT_EXPIRED)
                        .build();
            }
        }


        var cartDetails = cartDetailRepository.findAllById(request.getCartDetailIds());
        cartDetails = cartDetails.stream()
                .filter(c -> c.getCart().getId().equals(securityUtils.getPrincipal().getCartId()))
                .collect(Collectors.toUnmodifiableList());

        OrderEntity order = new OrderEntity();
        order.setDateOrder(new Timestamp(new Date().getTime()));
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var account = accountRepository.findByEmailFetchUser(email).orElseThrow(
                () -> new ObjectNotFoundException("email", email)
        );
        order.setUser(account.getUser());
        order.setDiscount(discount);
        order.setStatus(prefetchEntityProvider.getOrderStatusMap().get(OrderStatus.UNPAID.getId()));
        var orderSaved = orderRepository.save(order);

        List<OrderDetailEntity> listOrderDetailSave = new ArrayList<>();

        for (var cartDetail : cartDetails){
            OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
            orderDetailEntity.setOrder(orderSaved);
            orderDetailEntity.setBook(cartDetail.getBook());
            orderDetailEntity.setQuantity(cartDetail.getQuantity());
            listOrderDetailSave.add(orderDetailEntity);
        }

        orderDetailRepository.saveAll(listOrderDetailSave);
        cartDetailRepository.deleteAll(cartDetails);

        return Response.<OnlyIdDTO>newBuilder()
                .setData(OnlyIdDTO.builder()
                        .id(orderSaved.getId())
                        .build())
                .build();

    }

    @Override
    public Response<OrderDTO> getPreOder(PreOrderRequest request) {
        var principal = securityUtils.getPrincipal();
        var carts = cartDetailRepository.findAllByCartDetailIdsFetchBook(request.getCartDetailId(), principal.getCartId());
        DiscountEntity discount = null;
        if (request.getDiscountId() != null && request.getDiscountId() > 0){
            discount = discountRepository.findById(request.getDiscountId()).orElseThrow(
                    () -> new ObjectNotFoundException("discountId", request.getDiscountId())
            );
            Date now = new Date();
            if (discount.getStartDate().compareTo(now) > 0 || discount.getEndDate().compareTo(now) < 0){
                return Response.<OrderDTO>newBuilder()
                        .setSuccess(false)
                        .setMessage("Discount is expired!")
                        .setErrorCode(ErrorCode.DISCOUNT_EXPIRED)
                        .build();
            }
        }

        var orderBuilder = OrderDTO.newBuilder()
                .setTotalItem((long)carts.size())
                .setItems(carts.stream()
                        .map(c -> OrderDTO.OrderItems.newBuilder()
                                .setAuthor(c.getBook().getAuthor())
                                .setBookId(c.getBook().getId())
                                .setPrice(String.valueOf(c.getBook().getPrice()))
                                .setImage(c.getBook().getImage())
                                .setBookTitle(c.getBook().getTitle())
                                .setQuantity(c.getQuantity())
                                .build())
                        .collect(Collectors.toUnmodifiableList()));
        var totalMoney = carts.stream().reduce(0L, (a, b) -> a + b.getBook().getPrice() * b.getQuantity(), Long::sum);
        if (discount != null){
            orderBuilder.setDiscountDTO(DiscountDTO.newBuilder()
                    .setCode(discount.getCode())
                    .setDescription(discount.getDescription())
                    .setId(discount.getId())
                    .setName(discount.getName())
                    .setValue(discount.getValue())
                    .setStartDate(DateTimeUtils.timestamp2String(discount.getStartDate()))
                    .setEndDate(DateTimeUtils.timestamp2String(discount.getEndDate()))
                    .build());
            orderBuilder.setTotalMoney(totalMoney * (100 - discount.getValue()) / 100);
        } else {
            orderBuilder.setTotalMoney(totalMoney);
        }
        return Response.<OrderDTO>newBuilder()
                .setSuccess(true)
                .setData(orderBuilder.build())
                .build();

    }
}
