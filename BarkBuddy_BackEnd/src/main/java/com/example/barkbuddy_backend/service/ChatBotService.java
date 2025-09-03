package com.example.barkbuddy_backend.service;

//import com.fasterxml.jackson.databind.JsonNode;
//import com.theokanning.openai.service.OpenAiService;
//import com.theokanning.openai.completion.chat.ChatCompletionRequest;
//import com.theokanning.openai.completion.chat.ChatMessage;

//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.*;
//
//@Service
public class ChatBotService {

//    private final String apiKey;
//
//    public ChatBotService(@Value("${openai.api.key}") String apiKey) {
//        this.apiKey = apiKey;
//    }
//
//    public String ask(String prompt) {
//        String url = "https://api.openai.com/v1/chat/completions";
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Request body
//        Map<String, Object> body = new HashMap<>();
//        body.put("model", "gpt-3.5-turbo");
//        body.put("max_tokens", 200);
//        body.put("messages", new Object[]{Map.of("role", "user", "content", prompt)});
//
//        // Headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(apiKey);
//
//        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
//
//        ResponseEntity<JsonNode> response = restTemplate.exchange(
//                url,
//                HttpMethod.POST,
//                request,
//                JsonNode.class
//        );
//
//        if (response.getBody() != null) {
//            JsonNode choices = response.getBody().get("choices");
//            if (choices.isArray() && choices.size() > 0) {
//                return choices.get(0).get("message").get("content").asText();
//            }
//        }
//
//        return "Sorry, no response from AI.";
//    }
}
