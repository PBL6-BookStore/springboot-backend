package com.pbl6.bookstore.service.impl;

import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.chatbot.ChatBotAIDTO;
import com.pbl6.bookstore.payload.response.chatbot.ChatBotDTO;
import com.pbl6.bookstore.service.ChatBotService;
import com.pbl6.bookstore.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    private final SecurityUtils securityUtils;

    @Override
    public Response<ChatBotDTO> chatBot(String request) {
        var principal = securityUtils.getPrincipal();
        ChatBotAIDTO response = restTemplate.getForObject(chatBotAPI + "?message=" + request + "&userId=" + principal.getUserId(), ChatBotAIDTO.class);
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
