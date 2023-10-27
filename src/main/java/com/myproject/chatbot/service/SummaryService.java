package com.myproject.chatbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;

@Service
public class SummaryService {

    private static final String OUTPUT_FILE = "products-info.txt";
    private final ChatbotService chatbotService;

    @Autowired
    public SummaryService(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    public String getSummary() throws IOException {
        String productData = readProductData();
        String question = "Please provide a summary for the following product details:";
        String completePrompt = question + "\n" + productData;

        String summary = chatbotService.getResponse(completePrompt);

        // Save to file
        saveSummaryToFile(summary);

        return summary;
    }

    private String readProductData() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("products.txt");
        if (is == null) {
            throw new FileNotFoundException("Cannot find products.txt");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private void saveSummaryToFile(String summary) throws IOException {
        Path outputPath = Paths.get(OUTPUT_FILE);
        Files.write(outputPath, summary.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}
