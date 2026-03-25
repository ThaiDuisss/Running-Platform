// src/features/user/components/Chat/services/chatService.js
import axiosClient from "@/shared/services/axiosClient";

export const chatService = {
    getConversations: () =>
        axiosClient.get('/api/conversations'),

    getMessages: (conversationId, page = 0, size = 30) =>
        axiosClient.get(`/api/conversations/${conversationId}/messages`, {
            params: { page, size },
        }),

    createOrGetDirectChat: (targetUserId) =>
        axiosClient.post('/api/conversations/direct', { targetUserId }),

    createGroup: (title, memberIds) =>
        axiosClient.post('/api/conversations/group', { title, memberIds }),

    addMember: (conversationId, userId) =>
        axiosClient.post(`/api/conversations/${conversationId}/members`, { userId }),

    leaveGroup: (conversationId) =>
        axiosClient.delete(`/api/conversations/${conversationId}/leave`),

    searchUsers: (query) =>
        axiosClient.get('/users/search', { params: { keyword: query } }),
};