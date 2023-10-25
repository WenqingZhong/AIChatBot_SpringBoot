package com.myproject.chatbot.service;
import com.myproject.chatbot.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductFormatterService {

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
