package com.pbl6.bookstore.exception;

import lombok.AllArgsConstructor;

/**
 * @author lkadai0801
 * @since 26/10/2022
 */

@AllArgsConstructor
public class BookStoreException extends RuntimeException{
    protected final boolean isClientError;

    public boolean isClientError(){
        return isClientError;
    }
}
