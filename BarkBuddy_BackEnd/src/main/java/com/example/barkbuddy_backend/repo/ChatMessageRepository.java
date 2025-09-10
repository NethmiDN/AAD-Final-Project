package com.example.barkbuddy_backend.repo;

import com.example.barkbuddy_backend.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT m FROM ChatMessage m WHERE m.dogId = :dogId AND ((m.senderId = :u1 AND m.receiverId = :u2) OR (m.senderId = :u2 AND m.receiverId = :u1)) ORDER BY m.timestamp ASC")
    List<ChatMessage> findConversation(@Param("dogId") Long dogId,
                                       @Param("u1") Long user1,
                                       @Param("u2") Long user2);

    List<ChatMessage> findBySenderIdOrReceiverIdOrderByTimestampDesc(Long senderId, Long receiverId);
}
