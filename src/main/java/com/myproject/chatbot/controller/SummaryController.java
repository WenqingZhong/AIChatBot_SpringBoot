package com.myproject.chatbot.controller;

import com.myproject.chatbot.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/summary")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateSummary() {
        try {
            summaryService.generateAllSummaries();
            return ResponseEntity.ok("Summary generated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating summary: " + e.getMessage());
        }
    }
}





