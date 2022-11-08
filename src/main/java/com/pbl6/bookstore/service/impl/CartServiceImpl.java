package com.pbl6.bookstore.service.impl;

import com.pbl6.bookstore.domain.entity.CartEntity;
import com.pbl6.bookstore.domain.repository.jpa.CartRepository;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author vndat00
 * @since 05/11/2022
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    @Override
    public Response<OnlyIdDTO> addNewCart() {
        CartEntity cartEntity = new CartEntity();
        cartRepository.save(cartEntity);
        return Response.<OnlyIdDTO>newBuilder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .id(cartEntity.getId())
                        .build())
                .build();
    }
}
