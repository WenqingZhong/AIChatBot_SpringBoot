package com.myproject.chatbot.service;
import com.myproject.chatbot.model.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductFormatterService {

    private static final String SUMMARIES_FOLDER = "src/main/resources/summary";

    public String getProductsData(String filename) {
        return loadProductsData(filename);
    }

    private String loadProductsData(String filename) {
        try {
            Path filePath = Paths.get(SUMMARIES_FOLDER, filename + ".txt");
            return new String(Files.readAllBytes(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading the product summary file: " + e.getMessage(), e);
        }
    }
}
