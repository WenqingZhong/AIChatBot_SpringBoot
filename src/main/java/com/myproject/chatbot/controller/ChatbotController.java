package com.myproject.chatbot.controller;

import com.jayway.jsonpath.JsonPath;
import com.myproject.chatbot.service.ChatbotService;
import com.myproject.chatbot.service.ProductFormatterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ChatbotController {
    @Autowired
    private ChatbotService chatbotService;


    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askQuestion(@RequestBody String question) {
        String aiResponse = chatbotService.getResponse(question);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("response", aiResponse);
        return ResponseEntity.ok(responseBody);
    }
}
