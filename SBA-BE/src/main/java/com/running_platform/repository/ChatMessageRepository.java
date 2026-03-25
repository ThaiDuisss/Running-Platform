package com.running_platform.repository;

import com.running_platform.entity.FriendShipAndChat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findByConversationIdOrderByCreatDateDesc(Long conversationId, Pageable pageable);

    @Query("""
        SELECT m FROM ChatMessage m
        WHERE m.conversation.id = :conversationId
        ORDER BY m.creatDate DESC
        LIMIT 1
    """)
    Optional<ChatMessage> findLastMessageByConversationId(@Param("conversationId") Long conversationId);
}