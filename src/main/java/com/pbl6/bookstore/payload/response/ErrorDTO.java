package com.pbl6.bookstore.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pbl6.bookstore.common.enums.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

/**
 * @author lkadai0801
 * @since 26/10/2022
 */

@Getter
@Setter
public class ErrorDTO implements Serializable {
    @Schema(description = "Request field or resource that causes the error", required = true, example = "username")
    private String key;

    @Schema(description = "The reason related to above field that causes the error", required = true, example = "REQUIRED_FIELD_MISSING")
    private ErrorCode code;

    @Schema(description = "Request value that causes the error", required = false, example = "anhdai")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String value;

    public static ErrorDTO of(String key, ErrorCode code){
        ErrorDTO inst = new ErrorDTO();
        inst.setKey(key);
        inst.setCode(code);
        return inst;
    }

    public static ErrorDTO of(String key, String value, ErrorCode code){
        ErrorDTO inst = new ErrorDTO();
        inst.setKey(key);
        inst.setValue(value);
        inst.setCode(code);
        return inst;
    }
}
