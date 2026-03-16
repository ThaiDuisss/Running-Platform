package com.running_platform.mapper;


import com.running_platform.dto.request.ConversationRequest;
import com.running_platform.dto.response.ConversationResponse;
import com.running_platform.entity.FriendShipAndChat.Conversations;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConversationMapper extends BaseMapper<Conversations, ConversationRequest, ConversationResponse>{
}
