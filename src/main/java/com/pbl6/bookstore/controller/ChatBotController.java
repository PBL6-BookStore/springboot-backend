package com.pbl6.bookstore.controller;

import com.pbl6.bookstore.payload.response.Response;
import com.pbl6.bookstore.payload.response.chatbot.ChatBotDTO;
import com.pbl6.bookstore.service.ChatBotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author lkadai0801
 * @since 25/12/2022
 */

@RestController
@RequiredArgsConstructor
@Tag(name = "Chat bot", description = "Chat bot APIs")
public class ChatBotController {
    private final ChatBotService chatBotService;

    @GetMapping("/bot")
    @Operation(summary = "Chat with bot", description = "Chat with bot api")
    @Parameter(in = ParameterIn.QUERY, name = "message", description = "message request", example = "Hello")
    public Response<ChatBotDTO> chatBot(@RequestParam("message") String message){
        return chatBotService.chatBot(message);
    }
}
