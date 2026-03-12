import axiosClient from "@/shared/services/axiosClient";
const API = {
    MY_CONVERSATIONS: "/chat/my-conversations",
    CREATE_CONVERSATION: "/chat/create",
    CREATE_MESSAGE: "/chat/messages/create",
    GET_CONVERSATION_MESSAGES: "/chat/messages/getMessage",
}

export const getMyConversations = async () => {
    return await axiosClient.get(API.MY_CONVERSATIONS);
};

export const createConversation = async (data) => {
    return await axiosClient.post(
        API.CREATE_CONVERSATION, {
            type: data.type,
            participantIds: data.participantIds,
        }
    );
};


export const createMessage = async (data) => {
    return await axiosClient.post(
        API.CREATE_MESSAGE, {
            conversationId: data.conversationId,
            message: data.message,
        }
    );
};

export const getMessages = async (conversationId) => {
    return await axiosClient.get(`${API.GET_CONVERSATION_MESSAGES}/${conversationId}`);
};