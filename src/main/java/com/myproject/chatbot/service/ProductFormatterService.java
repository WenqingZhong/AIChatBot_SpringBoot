package com.myproject.chatbot.service;
import com.myproject.chatbot.model.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class ProductFormatterService {
    @Value("classpath:products.txt")
    Resource productsFile;
    private String productsData;

    @PostConstruct
    public void init() {
        loadProductsData();
    }
    public String getProductsData() {
        return productsData;
    }

    private void loadProductsData() {
        try {
            productsData = new String(Files.readAllBytes(productsFile.getFile().toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    public String formatProducts(List<Product> products) {
        StringBuilder formatted = new StringBuilder("Products:\n");
        int counter = 1;
        for (Product product : products) {
            formatted.append(counter)
                    .append(". ID: ").append(product.getId())
                    .append(", Name: ").append(product.getName())
                    .append(", Description: ").append(product.getDescription())
                    .append("\n");
            counter++;
        }
        return formatted.toString();
    }
}
