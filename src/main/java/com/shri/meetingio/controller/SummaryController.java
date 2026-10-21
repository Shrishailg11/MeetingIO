package com.shri.meetingio.controller;

import com.shri.meetingio.dto.SummaryResponse;
import com.shri.meetingio.service.SummaryService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/summaries")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SummaryResponse summarize(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transcript file is empty");
        }
        if (fileName == null || !fileName.toLowerCase().endsWith(".txt")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only .txt transcripts are supported");
        }

        String transcript = new String(file.getBytes(), StandardCharsets.UTF_8);
        if (transcript.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transcript contains no text");
        }

        String summary = summaryService.summarize(transcript);
        return new SummaryResponse(fileName, transcript.length(), summary);
    }
}