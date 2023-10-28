package com.myproject.chatbot.controller;

import com.jayway.jsonpath.JsonPath;
import com.myproject.chatbot.service.ChatbotService;
import com.myproject.chatbot.service.ProductFormatterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ChatbotController {
    @Autowired
    private ChatbotService chatbotService;

    @GetMapping("/products")
    public ResponseEntity<Map<String, String>> getAvailableProducts() {
        String products = chatbotService.getAvailableProducts();
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("products", products);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askQuestion(@RequestBody Map<String, String> request) {
        String productChoice = request.get("product");
        String question = request.get("question");
        String aiResponse = chatbotService.processUserChoice(productChoice, question);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("response", aiResponse);
        return ResponseEntity.ok(responseBody);
    }

//    @PostMapping("/ask")
//    public ResponseEntity<Map<String, String>> askQuestion(@RequestBody String question) {
//        String aiResponse = chatbotService.getResponse(question);
//        Map<String, String> responseBody = new HashMap<>();
//        responseBody.put("response", aiResponse);
//        return ResponseEntity.ok(responseBody);
//    }
}
