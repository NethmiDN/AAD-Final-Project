package com.example.barkbuddy_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
