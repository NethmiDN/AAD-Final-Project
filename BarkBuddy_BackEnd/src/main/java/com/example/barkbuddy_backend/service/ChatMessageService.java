package com.example.barkbuddy_backend.service;

import com.example.barkbuddy_backend.entity.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    ChatMessage sendMessage(Long dogId, Long senderId, Long receiverId, String content, String imageUrl);
    List<ChatMessage> getConversation(Long dogId, Long user1, Long user2);
    List<com.example.barkbuddy_backend.dto.ChatConversationDTO> listConversations(Long userId);
}
