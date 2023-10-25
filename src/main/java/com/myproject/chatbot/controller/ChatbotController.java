package com.myproject.chatbot.controller;

import com.jayway.jsonpath.JsonPath;
import com.myproject.chatbot.model.Product;
import com.myproject.chatbot.service.ChatbotService;
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

    private List<Product> products = Arrays.asList(
            new Product("1", "Product A", "Description for Product A"),
            new Product("2", "Product B", "Description for Product B")
            // ... add more products as needed
    );

    @PostMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestBody String question) {
        // Logic to interface with AI (e.g., OpenAI's API)
        //String aiResponse = "Sample Response";  // Dummy response for now
        String aiResponse = chatbotService.getResponse(question);
        String responseText = JsonPath.parse(aiResponse).read("$.choices[0].text", String.class).trim();
        return ResponseEntity.ok(responseText);
    }

    @GetMapping("/productInfo")
    public ResponseEntity<List<Product>> getProductInfo() {
        return ResponseEntity.ok(products);
    }
}
