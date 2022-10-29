package com.pbl6.bookstore.exception;

import com.pbl6.bookstore.payload.response.ErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author lkadai0801
 * @since 29/10/2022
 */
@Getter
public class ValidateException extends RuntimeException{
    private final String message = "Validate failure";
    private final List<ErrorDTO> errors;

    public ValidateException(List<ErrorDTO> errors){
        super();
        this.errors = errors;
    }
}
