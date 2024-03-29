package com.pbl6.bookstore.service.impl;

import com.pbl6.bookstore.common.enums.ErrorCode;
import com.pbl6.bookstore.domain.entity.CartEntity;
import com.pbl6.bookstore.domain.entity.UserEntity;
import com.pbl6.bookstore.domain.repository.jpa.AccountRepository;
import com.pbl6.bookstore.domain.repository.jpa.CartRepository;
import com.pbl6.bookstore.domain.repository.jpa.UserRepository;
import com.pbl6.bookstore.exception.ObjectNotFoundException;
import com.pbl6.bookstore.payload.request.UserRequest;
import com.pbl6.bookstore.payload.response.*;
import com.pbl6.bookstore.payload.response.user.UserDTO;
import com.pbl6.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lkadai0801
 * @since 02/11/2022
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CartRepository cartRepository;

    @Override
    public Response<ListDTO<UserDTO>> getAllUser() {
        var accounts = accountRepository.findAll();
        return Response.<ListDTO<UserDTO>>newBuilder()
                .setSuccess(true)
                .setData(ListDTO.<UserDTO>newBuilder()
                        .setTotalElements((long)accounts.size())
                        .setItems(accounts.stream()
                                .map(u -> UserDTO.newBuilder()
                                        .setEmail(u.getEmail())
                                        .setUsername(u.getEmail().substring(0, u.getEmail().indexOf("@")))
                                        .setPassword(u.getPassword())
                                        .build())
                                .collect(Collectors.toUnmodifiableList()))
                        .build())
                .build();
    }

    @Override
    public Response<OnlyIdDTO> addNewUser(UserRequest request) {
        List<ErrorDTO> errors = new ArrayList<>();
        validateAddUser(request, errors);
        return null;
    }

    @Override
    public Response<OnlyIdDTO> addNewUserDependOnCart(Long cartId) {
        UserEntity user = new UserEntity();
        CartEntity cart = cartRepository.findById(cartId).orElseThrow(() ->
                new ObjectNotFoundException("Can't find cart with id {} in database.", cartId));
//        user.setCart(cart);
        user.setFirstName("");
        user.setLastName("");
        userRepository.save(user);
        return Response.<OnlyIdDTO>newBuilder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .id(user.getId())
                        .build())
                .build();
    }

    private void validateAddUser(UserRequest request, List<ErrorDTO> errorDTOS){
        if (request.getEmail() == null){
            errorDTOS.add(ErrorDTO.of("email", ErrorCode.NOT_NULL));
        } else if (!StringUtils.hasText(request.getEmail())){
            errorDTOS.add(ErrorDTO.of("email", ErrorCode.NOT_EMPTY));
        }
    }
}
