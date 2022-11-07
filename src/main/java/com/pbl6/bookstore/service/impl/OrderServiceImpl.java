package com.pbl6.bookstore.service.impl;

import com.pbl6.bookstore.common.enums.ErrorCode;
import com.pbl6.bookstore.common.enums.OrderStatus;
import com.pbl6.bookstore.domain.entity.DiscountEntity;
import com.pbl6.bookstore.domain.entity.OrderDetailEntity;
import com.pbl6.bookstore.domain.entity.OrderEntity;
import com.pbl6.bookstore.domain.prefetch.PrefetchEntityProvider;
import com.pbl6.bookstore.domain.repository.jpa.CartDetailRepository;
import com.pbl6.bookstore.domain.repository.jpa.DiscountRepository;
import com.pbl6.bookstore.domain.repository.jpa.OrderDetailRepository;
import com.pbl6.bookstore.domain.repository.jpa.OrderRepository;
import com.pbl6.bookstore.exception.ObjectNotFoundException;
import com.pbl6.bookstore.payload.request.NewOrderRequest;
import com.pbl6.bookstore.payload.response.ErrorDTO;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private final PrefetchEntityProvider prefetchEntityProvider;

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

        if (request.getDiscountId() != null){
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

        OrderEntity order = new OrderEntity();
        order.setDateOrder(new Timestamp(new Date().getTime()));
//        order.setUser(myUser);
        order.setDiscount(discount);
        order.setStatus(prefetchEntityProvider.getOrderStatusMap().get(OrderStatus.PAID.getId()));
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

        return Response.<OnlyIdDTO>newBuilder()
                .setData(OnlyIdDTO.builder()
                        .id(orderSaved.getId())
                        .build())
                .build();

    }
}
