package com.example.barkbuddy_backend.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatConversationDTO {
    private Long dogId;
    private String dogName;
    private Long otherUserId;
    private String otherUsername;
    private String lastMessage;
    private Timestamp lastTimestamp;
}
