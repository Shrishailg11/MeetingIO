package com.shri.meetingio.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {

    private final ChatClient chatClient;
    private final Resource systemPrompt;
    private final Resource userPrompt;

    public SummaryService(
            ChatClient.Builder builder,
            @Value("classpath:/prompts/summary-system.st") Resource systemPrompt,
            @Value("classpath:/prompts/summary-user.st") Resource userPrompt) {
        this.chatClient = builder.build();
        this.systemPrompt = systemPrompt;
        this.userPrompt = userPrompt;
    }

    public String summarize(String transcript) {
        return chatClient.prompt()
                .system(systemPrompt)
                .user(u -> u.text(userPrompt).param("transcript", transcript))
                .call()
                .content();
    }
}