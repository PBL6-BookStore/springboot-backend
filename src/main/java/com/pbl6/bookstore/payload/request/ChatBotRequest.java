package com.pbl6.bookstore.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lkadai0801
 * @since 25/12/2022
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatBotRequest {
    private String message;
}
