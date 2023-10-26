package com.myproject.chatbot.service;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class SummaryService {

    @Autowired
    private ProductFormatterService productFormatterService;
    private WebClient webClient;
    @Autowired
    public SummaryService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.openai.com")
                .build();
    }

    public String generateSummary() {
        try {
            String productsData = productFormatterService.getProductsData();
            System.out.println(productsData);
            String test = "Product ID: 1\n" +
                    "Product name: sweater knit blanket\n" +
                    "Product category: bedding\n" +
                    "Product URL: https://www.saatva.com/bedding/sweater-knit-blanket";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("prompt", "Extract and provide the product name from the following information:"+ test);
            requestBody.put("max_tokens", 50);
            requestBody.put("temperature", 0.2);

            //System.out.println(requestBody);

            String apiResponse = webClient.post()
                    .uri("/v1/engines/davinci/completions") // Adjust if necessary
                    .header("Authorization", "Bearer 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")
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            String summary = JsonPath.read(apiResponse, "$.choices[0].text");
            System.out.println(summary);

            return summary;
        }catch(Exception e) {
            // Log the detailed error message and stack trace
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }

    public void saveSummaryToFile(String summary) {
        //save 'summary' to 'products_info.txt'
        Path path = Paths.get("products_info.txt");
        try {
            Files.write(path, summary.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
