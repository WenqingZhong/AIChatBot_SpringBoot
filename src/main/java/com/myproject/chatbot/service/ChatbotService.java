package com.myproject.chatbot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatbotService {

    private final WebClient webClient;

    public ChatbotService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com") // Or any other AI API endpoint
                .build();
    }

    public String getResponse(String question) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", "Respond briefly and concisely to the query: "+ question);
        requestBody.put("max_tokens", 100);
        requestBody.put("temperature", 0.2);

        String apiResponse = webClient.post()
                .uri("/v1/engines/davinci/completions") // This is a hypothetical endpoint, refer to actual API docs
                .header("Authorization", "Bearer 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") // Replace with your API key
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Process the apiResponse if needed, extract relevant data, etc.
        return apiResponse; // Return the processed response
    }
}

