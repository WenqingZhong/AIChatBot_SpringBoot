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
            //System.out.println(productsData);
            String test = "Product ID is 1\n" +
                    "Product name is sweater knit blanket\n" +
                    "Product category is bedding\n" +
                    "Product URL is  https://www.saatva.com/bedding/sweater-knit-blanket"+
                    "Product details are " +
                    "Home\n" +
                    "arrowBedding\n" +
                    "Sweater Knit Blanket\n" +
                    "slide page 1 of 6\n" +
                    "home-trial\n" +
                    "45-day free returnsLearn MoreChevron Right\n" +
                    "mattress-removal\n" +
                    "Free shippingLearn MoreChevron Right\n" +
                    "warranty\n" +
                    "1-year limited warrantyLearn MoreChevron Right\n" +
                    "Sweater Knit Blanket\n" +
                    "A supremely soft blanket that’s as cozy as your favorite sweater";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("prompt", "Write a summary for each product using the following product information:"+ productsData);
            requestBody.put("max_tokens", 50);
            requestBody.put("temperature", 0.5);

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
