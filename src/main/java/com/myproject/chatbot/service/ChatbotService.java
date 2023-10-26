package com.myproject.chatbot.service;

import com.myproject.chatbot.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatbotService {

    private final WebClient webClient;

    @Autowired
    private ProductFormatterService productFormatterService;
    //private List<Product> products;

    public ChatbotService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com")
                .build();

        this.productFormatterService = new ProductFormatterService();

//        this.products = Arrays.asList(
//                new Product("1", "Product A", "I am a dog"),
//                new Product("2", "Product B", "I am a cat")
//        );
    }

    public String getResponse(String question) {
        String productsData = productFormatterService.getProductsData();
        //String formattedProducts = productFormatterService.formatProducts(products);
        String completePrompt = "Given these products: \n"+ productsData + "\n\n" + "Please answer the following question:"+ question;

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

