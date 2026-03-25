package com.running_platform.repository;

import com.running_platform.entity.FriendShipAndChat.Conversations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversations, Long> {

        Optional<Conversations> findByConversationHash(String hash);

        /**
         * Lấy tất cả conversation mà user tham gia, sắp xếp theo tin nhắn mới nhất
         */
        @Query("""
        SELECT DISTINCT c FROM Conversations c
        JOIN c.conversationParticipants cp
        WHERE cp.user.id = :userId
        ORDER BY c.updateDate DESC
    """)
        List<Conversations> findAllByParticipantUserId(@Param("userId") Long userId);

        /**
         * Kiểm tra user có trong conversation không
         */
        @Query("""
        SELECT COUNT(cp) > 0 FROM ConversationParticipants cp
        WHERE cp.conversation.id = :conversationId
        AND cp.user.id = :userId
    """)
        boolean isUserInConversation(@Param("conversationId") Long conversationId,
                                     @Param("userId") Long userId);
}