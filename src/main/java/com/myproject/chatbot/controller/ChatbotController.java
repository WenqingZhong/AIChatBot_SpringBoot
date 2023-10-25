package com.myproject.chatbot.controller;

import com.jayway.jsonpath.JsonPath;
import com.myproject.chatbot.model.Product;
import com.myproject.chatbot.service.ChatbotService;
import com.myproject.chatbot.service.ProductFormatterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ChatbotController {
    @Autowired
    private ChatbotService chatbotService;


    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestBody String question) {
        String aiResponse = chatbotService.getResponse(question);
        String responseText = JsonPath.parse(aiResponse).read("$.choices[0].text", String.class).trim();
        return ResponseEntity.ok(responseText);
    }

  /*
    @GetMapping("/productInfo")
    public ResponseEntity<List<Product>> getProductInfo() {
        return ResponseEntity.ok(products);
    }

   */
}
