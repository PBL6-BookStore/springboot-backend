package com.pbl6.bookstore.service;

import com.pbl6.bookstore.payload.request.UserRequest;
import com.pbl6.bookstore.payload.response.ListDTO;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.user.UserDTO;

/**
 * @author lkadai0801
 * @since 02/11/2022
 */

public interface UserService {
    Response<ListDTO<UserDTO>> getAllUser();
    Response<OnlyIdDTO> addNewUser(UserRequest request);
}
