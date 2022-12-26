package com.pbl6.bookstore.service;

import com.pbl6.bookstore.payload.request.ChatBotRequest;
import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.chatbot.ChatBotDTO;

/**
 * @author lkadai0801
 * @since 25/12/2022
 */
public interface ChatBotService {
    Response<ChatBotDTO> chatBot(String request);
}
