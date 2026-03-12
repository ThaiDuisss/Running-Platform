package com.laundry.repository;

import com.laundry.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    @Query(value = "{'conversationId' : ?0}", sort = "{'createDate' : -1}")
    List<ChatMessage> findByConversationId(String conversationId);
}
