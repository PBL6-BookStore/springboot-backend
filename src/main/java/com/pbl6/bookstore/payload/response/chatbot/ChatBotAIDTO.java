package com.pbl6.bookstore.payload.response.chatbot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author lkadai0801
 * @since 25/12/2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatBotAIDTO implements Serializable {
    private Boolean success;
    private String data;
}
