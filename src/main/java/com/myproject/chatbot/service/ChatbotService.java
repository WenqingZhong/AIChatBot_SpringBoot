package com.myproject.chatbot.service;

import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class ChatbotService {
    private static final String URL = "https://api.openai.com/v1/chat/completions";
    private String API_KEY = "XXX";
    private static final String MODEL = "gpt-3.5-turbo";

    @Autowired
    private ProductFormatterService productFormatterService;

    public ChatbotService() {
        this.productFormatterService = new ProductFormatterService();
    }

    private static final List<String> VALID_PRODUCTS =
            List.of( "organic sateen duvet cover set", "flannel sheet set", "embroidered hotel style sheets",
                    "memory foam hybrid", "aero quilt", "waffle knit duvet cover set", "silk eye mask", "herringbone knit blanket",
                    "saatva hd", "diamond knit blanket", "embroidered sateen duvet set",
                    "linen sheet set", "solaire", "organic percale duvet cover set", "banded percale sheet set",
                    "saatva latex hybrid", "weighted silk eye mask", "percale sheet set", "essential sheet set", "saatva rx",
                    "waffle knit blanket", "sweater knit blanket", "weighted blanket", "waffle towels", "organic cotton sheets",
                    "saatva classic", "organic cotton channel quilt", "zenhaven", "loom and leaf", "sateen cotton sheet set",
                    "banded percale duvet set", "saatva youth", "plush towels", "organic velvet quilt", "linen duvet cover set");


    public String getAvailableProducts() {
        return String.join(", ", VALID_PRODUCTS);
    }

    public String processUserChoice(String userChoice, String question) {
        if (VALID_PRODUCTS.contains(userChoice.toLowerCase())) {
            String productsData = productFormatterService.getProductsData(userChoice);
            return generateResponse(question, productsData);
        } else {
            return "Invalid product. Please pick a valid product.";
        }
    }

    private String generateResponse(String question, String productsData) {
        String completePrompt = "Please answer the question: " + question + " using the following information: " + productsData + "\n Please speak casually like a real person. Provide the full redirect product URL in the provided data if the URL starts with 'http'.";
        String escapedPrompt = completePrompt.replace("\n", "\\n").replace("\"", "\\\"");
        return chatGPT(escapedPrompt);
    }

    public String getSummary(String productInfo) {
        //String productsData = productFormatterService.getProductsData();
        System.out.println("I'm in getSummary");

        String completePrompt = "Please summarize the product information in bullet point fashion using the following data, and include Product URL in the summary if you see Product URL: "+ productInfo;

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