package com.myproject.chatbot.service;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ChatbotService {
    private static final String URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "XXX"; // Ensure to add your actual key here
    private static final String MODEL = "gpt-3.5-turbo";

    @Autowired
    private ProductFormatterService productFormatterService;

    public ChatbotService() {
        this.productFormatterService = new ProductFormatterService();
    }

    public String getResponse(String question) {
        String productsData = productFormatterService.getProductsData();
        String completePrompt = "Please answer this question: " + question;

        return chatGPT(completePrompt);
    }

    private String chatGPT(String prompt) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Constructing the request body
            String body = "{\"model\": \"" + MODEL + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Reading the response from ChatGPT
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            return extractMessageFromJSONResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractMessageFromJSONResponse(String response) {
        return JsonPath.parse(response).read("$.choices[0].message.content", String.class);
    }
}















//import com.myproject.chatbot.model.Product;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

//@Service
//public class ChatbotService {
//
//    private final WebClient webClient;
//
//    @Autowired
//    private ProductFormatterService productFormatterService;
//    //private List<Product> products;
//
//    public ChatbotService() {
//        this.webClient = WebClient.builder()
//                .baseUrl("https://api.openai.com")
//                .build();
//
//        this.productFormatterService = new ProductFormatterService();
//
//    }
//
//    public String getResponse(String question) {
//        String productsData = productFormatterService.getProductsData();
//        //String formattedProducts = productFormatterService.formatProducts(products);
//        String completePrompt = "Please answer this question:" +question+ "using these product information :"+ productsData + "\n" +"Do not ask any questions back or try to reach the max answer length with irrelevant information." ;
//
//        Map<String, Object> requestBody = new HashMap<>();
//        requestBody.put("prompt", completePrompt);
//        requestBody.put("max_tokens", 100);
//        requestBody.put("temperature", 0.2);
//
//        System.out.println(completePrompt);
//        System.out.println(requestBody);
//
//        String apiResponse = webClient.post()
//                .uri("/v1/engines/davinci/completions") // This is a hypothetical endpoint, refer to actual API docs
//                .header("Authorization", "Bearer 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")
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        return apiResponse;
//    }
//}

