package com.myproject.chatbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class SummaryService {

    @Autowired
    private ChatbotService chatbotService;

    private static final int CHUNK_SIZE = 100; // Number of lines per chunk

    private static final String PRODUCTS_FOLDER = "src/main/resources/products";
    private static final String SUMMARIES_FOLDER = "src/main/resources/summary";

    public void generateAllSummaries() throws IOException {
        Path productsDir = Paths.get(PRODUCTS_FOLDER);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(productsDir, "*.txt")) {
            for (Path entry : stream) {
                String fileName = entry.getFileName().toString();
                String outputPath = Paths.get(SUMMARIES_FOLDER, fileName).toString();
                generateSummary(entry.toString(), outputPath);
            }
        }
    }

    public void generateSummary(String inputFilePath, String outputFilePath) {

        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

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

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing file: " + e.getMessage(), e);
        }
    }

    private void processAndWriteChunk(List<String> chunk, BufferedWriter writer) throws IOException {

        String chunkAsString = String.join("\n", chunk);
        String summary = chatbotService.getSummary(chunkAsString);
        writer.write(summary);
        writer.newLine(); // Separate summaries by a new line
    }
}