package com.pbl6.bookstore.service;

import com.pbl6.bookstore.payload.request.AccountRequest;
import com.pbl6.bookstore.payload.response.OnlyIdDTO;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.account.AccountDTO;

/**
 * @author vndat00
 * @since 05/11/2022
 */

public interface AccountService {
    Response<OnlyIdDTO> addNewAccount(AccountRequest request);
    void addRoleToAccount(String email, String rolename);
    Response<AccountDTO> getAccountByEmail(String email);

}
