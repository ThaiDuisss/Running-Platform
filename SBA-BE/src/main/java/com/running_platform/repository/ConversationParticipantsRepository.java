package com.running_platform.repository;

import com.running_platform.entity.FriendShipAndChat.ConversationParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationParticipantsRepository extends JpaRepository<ConversationParticipants, Long> {

    List<ConversationParticipants> findByConversationId(Long conversationId);

    Optional<ConversationParticipants> findByUserIdAndConversationId(Long userId, Long conversationId);

    boolean existsByUserIdAndConversationId(Long userId, Long conversationId);
}