package com.pbl6.bookstore.payload.response.chatbot;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lkadai0801
 * @since 25/12/2022
 */

@Getter
@Setter
@Builder(builderClassName = "newBuilder", setterPrefix = "set")
public class ChatBotDTO {
    private String message;
}
