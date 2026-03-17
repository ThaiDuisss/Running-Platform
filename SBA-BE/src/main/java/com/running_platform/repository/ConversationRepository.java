package com.running_platform.repository;

import com.running_platform.entity.FriendShipAndChat.Conversations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversations, Long> {
        //        List<Conversations> findBy(Long userId);

        Optional<Conversations> findByConversationHash(String conversationHash);
}
