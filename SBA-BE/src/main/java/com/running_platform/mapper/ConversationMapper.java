package com.laundry.mapper;

import com.laundry.dto.request.ConversationRequest;
import com.laundry.dto.response.ConversationResponse;
import com.laundry.entity.Conversation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConversationMapper extends BaseMapper<Conversation, ConversationRequest, ConversationResponse>{
}
