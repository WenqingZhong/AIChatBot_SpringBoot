package com.myproject.chatbot.service;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private String API_KEY = "XXX"; // Ensure to add your actual key here
    private static final String MODEL = "gpt-3.5-turbo";

    @Autowired
    private ProductFormatterService productFormatterService;

    public ChatbotService() {
        this.productFormatterService = new ProductFormatterService();
    }

    public String getResponse(String question) {
        String productsData = productFormatterService.getProductsData();
        System.out.println(productsData);

        String completePrompt = "Please answer the question:  " + question + "using the following information: "+ productsData;
        //String completePrompt = "Please answer the question:  " + question;

        String escapedPrompt = completePrompt.replace("\n", "\\n").replace("\"", "\\\"");

        return chatGPT(escapedPrompt);
    }

    public String getSummary(String productInfo) {
        //String productsData = productFormatterService.getProductsData();
        System.out.println("I'm in getSummary");

        String completePrompt = "Please summarize each product using the following information: "+ productInfo;
        //String completePrompt = "Please answer the question:  " + question;

        String escapedPrompt = completePrompt.replace("\n", "\\n").replace("\"", "\\\"");
        //System.out.println("This is prompt"+escapedPrompt);

        return chatGPT(escapedPrompt);
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
            System.out.println("I'm in chatGPT");

            return extractMessageFromJSONResponse(response.toString());

        } catch (IOException e) {
            System.out.println("I'm in chatGPT error");
            throw new RuntimeException(e);
        }
    }

    private String extractMessageFromJSONResponse(String response) {
        return JsonPath.parse(response).read("$.choices[0].message.content", String.class);
    }
}