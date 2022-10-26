package com.pbl6.bookstore.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pbl6.bookstore.common.enums.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author lkadai0801
 * @since 26/10/2022
 */

@Getter
@Setter
@Builder(builderMethodName = "newBuilder", setterPrefix = "set")
public class Response<T> implements Serializable {
    private Boolean success;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private ErrorCode errorCode;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ErrorDTO> errors;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String exception;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T data;
}
