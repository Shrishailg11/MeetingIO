package com.shri.meetingio.dto;

public record SummaryResponse(
        String fileName,
        int transcriptLength,
        String summary
) {
}