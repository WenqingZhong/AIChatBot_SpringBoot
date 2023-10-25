package com.myproject.chatbot.repository;

import com.myproject.chatbot.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    public Product fetchProductFromDatabase(String productId) {
        // Database logic here
        return new Product();
    }
}
