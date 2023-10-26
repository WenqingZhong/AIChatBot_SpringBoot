package com.myproject.chatbot.controller;

import com.myproject.chatbot.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SummaryController {
    private final SummaryService summaryService;
    @Autowired
    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }
    @GetMapping("/generate-summary")
    public String generateSummary() {
        String summary = summaryService.generateSummary();
        summaryService.saveSummaryToFile(summary);
        return "Summary generated and saved to products_info.txt successfully!";
    }
}