package com.myproject.chatbot.service;

import com.myproject.chatbot.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatbotService {

    private final WebClient webClient;
    private final ProductFormatterService productFormatterService;
    private List<Product> products;

    public ChatbotService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com")
                .build();

        this.productFormatterService = new ProductFormatterService();

        this.products = Arrays.asList(
                new Product("1", "Product A", "I am a dog"),
                new Product("2", "Product B", "I am a cat")
        );
    }

    public String getResponse(String question) {
        String formattedProducts = productFormatterService.formatProducts(products);
        String completePrompt = "Given these products: \n"+ formattedProducts + "\n\n" + "Please answer the following question:"+ question;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", completePrompt);
        requestBody.put("max_tokens", 100);
        requestBody.put("temperature", 0.2);

        System.out.println(completePrompt);
        System.out.println(requestBody);

        String apiResponse = webClient.post()
                .uri("/v1/engines/davinci/completions") // This is a hypothetical endpoint, refer to actual API docs
                .header("Authorization", "Bearer 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")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return apiResponse;
    }
}

