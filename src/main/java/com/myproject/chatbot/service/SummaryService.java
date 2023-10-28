package com.myproject.chatbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SummaryService {

    @Autowired
    private ChatbotService chatbotService;

    private static final int CHUNK_SIZE = 100; // Number of lines per chunk

    private static final String INPUT_PATH = "src/main/resources/products.txt";
    private static final String OUTPUT_PATH = "src/main/resources/products-info.txt";

    public void generateSummary() {

        File inputFile = new File(INPUT_PATH);
        File outputFile = new File(OUTPUT_PATH);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            List<String> chunk = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                chunk.add(line);

                if (chunk.size() == CHUNK_SIZE) {
                    processAndWriteChunk(chunk, writer);
                    chunk.clear();
                }
            }

            // Handle the last chunk (which might be smaller than CHUNK_SIZE)
            if (!chunk.isEmpty()) {
                processAndWriteChunk(chunk, writer);
            }

        }catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error processing file: " + e.getMessage(), e);
        }
    }

    private void processAndWriteChunk(List<String> chunk, BufferedWriter writer) throws IOException {

        String chunkAsString = String.join("\n", chunk);
        //System.out.println("This is chunk"+ chunkAsString);
        String summary = chatbotService.getSummary(chunkAsString);
        writer.write(summary);
        writer.newLine(); // Separate summaries by a new line
    }
}