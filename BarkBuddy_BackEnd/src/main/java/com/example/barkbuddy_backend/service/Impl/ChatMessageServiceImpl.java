package com.example.barkbuddy_backend.service.Impl;

import com.example.barkbuddy_backend.entity.ChatMessage;
import com.example.barkbuddy_backend.entity.Dog;
import com.example.barkbuddy_backend.entity.User;
import com.example.barkbuddy_backend.dto.ChatConversationDTO;
import com.example.barkbuddy_backend.repo.ChatMessageRepository;
import com.example.barkbuddy_backend.repo.DogRepository;
import com.example.barkbuddy_backend.repo.UserRepository;
import com.example.barkbuddy_backend.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatRepo;
    private final DogRepository dogRepo;
    private final UserRepository userRepo;

    @Override
    public ChatMessage sendMessage(Long dogId, Long senderId, Long receiverId, String content, String imageUrl) {
        ChatMessage msg = ChatMessage.builder()
                .dogId(dogId)
                .senderId(senderId)
                .receiverId(receiverId)
                .content(content)
                .imageUrl(imageUrl)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        return chatRepo.save(msg);
    }

    @Override
    public List<ChatMessage> getConversation(Long dogId, Long user1, Long user2) {
        return chatRepo.findConversation(dogId, user1, user2);
    }

    @Override
    public List<ChatConversationDTO> listConversations(Long userId) {
        // Fetch all messages involving user ordered desc by timestamp
        List<ChatMessage> messages = chatRepo.findBySenderIdOrReceiverIdOrderByTimestampDesc(userId, userId);
        java.util.Map<String, ChatConversationDTO> map = new java.util.LinkedHashMap<>();
        for(ChatMessage m : messages){
            Long other = m.getSenderId().equals(userId) ? m.getReceiverId() : m.getSenderId();
            
            // Skip self-conversations
            if(other.equals(userId)) {
                continue;
            }
            
            String key = m.getDogId()+":"+other;
            if(!map.containsKey(key)){
                Dog dog = dogRepo.findById(m.getDogId()).orElse(null);
                User otherUser = userRepo.findById(other).orElse(null);
        String preview = (m.getContent()==null || m.getContent().isBlank()) && m.getImageUrl()!=null ? "[Image]" : m.getContent();
        map.put(key, ChatConversationDTO.builder()
                        .dogId(m.getDogId())
                        .dogName(dog!=null?dog.getDogName():"Unknown Dog")
                        .otherUserId(other)
                        .otherUsername(otherUser!=null?otherUser.getUsername():"Unknown User")
            .lastMessage(preview)
                        .lastTimestamp(m.getTimestamp())
                        .build());
            }
        }
        return new java.util.ArrayList<>(map.values());
    }
}
