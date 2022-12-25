package com.pbl6.bookstore.service.impl;

import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.chatbot.ChatBotAIDTO;
import com.pbl6.bookstore.payload.response.chatbot.ChatBotDTO;
import com.pbl6.bookstore.service.ChatBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lkadai0801
 * @since 25/12/2022
 */

@Service
@RequiredArgsConstructor
public class ChatBotServiceImpl implements ChatBotService {

    @Value("${pbl6bookstore.url.chatbot-service}")
    private String chatBotAPI;

    private final RestTemplate restTemplate;

    @Override
    public Response<ChatBotDTO> chatBot(String request) {
        Map<String, String> params = new HashMap<>();
        params.put("message", request);
        ChatBotAIDTO response = restTemplate.getForObject(chatBotAPI, ChatBotAIDTO.class, params);
        assert response != null;
        if (response.getSuccess()){
            return Response.<ChatBotDTO>newBuilder()
                    .setSuccess(true)
                    .setData(ChatBotDTO.builder()
                            .setMessage(response.getData())
                            .build())
                    .build();
        } else {
            return Response.<ChatBotDTO>newBuilder()
                    .setSuccess(false)
                    .setMessage(response.getData())
                    .build();
        }
    }
}
