// src/features/user/components/Chat/context/ChatContext.jsx
import { createContext, useContext, useState, useEffect, useCallback, useRef } from 'react';
import { useWebSocket } from '../hooks/useWebSocket';
import { chatService } from '../services/chatService';
import { AuthDataContext } from '@/app/providers/AuthProvider';

const ChatContext = createContext(null);

export function ChatProvider({ children }) {
    const { user: currentUser } = useContext(AuthDataContext);
    const { connected, subscribe, publish } = useWebSocket();

    const [conversations, setConversations] = useState([]);
    const [activeConvId, setActiveConvId] = useState(null);
    const [messages, setMessages] = useState({}); // { [convId]: ChatMessageResponse[] }
    const [loadingConvs, setLoadingConvs] = useState(false);
    const [loadingMsgs, setLoadingMsgs] = useState(false);
    const [pageInfo, setPageInfo] = useState({}); // { [convId]: { page, hasMore } }

    const subRefs = useRef({}); // track active subscriptions

    //Load conversations on mount
    useEffect(() => {
        if (!currentUser?.id) return;
        setLoadingConvs(true);
        chatService.getConversations()
            .then(res => setConversations(res.data))
            .catch(console.error)
            .finally(() => setLoadingConvs(false));
    }, [currentUser?.id]);

    //Subscribe to personal queue
    useEffect(() => {
        if (!connected || !currentUser?.id) return;
        let sub;
        subscribe(`/user/${currentUser.id}/queue/conversations`, (newConv) => {
            setConversations(prev => {
                if (prev.find(c => c.id === newConv.id)) return prev;
                return [newConv, ...prev];
            });
        }).then(s => { sub = s; });
        return () => sub?.unsubscribe();
    }, [connected, currentUser?.id, subscribe]);

    //Subscribe to active conversation topic 
    useEffect(() => {
        if (!connected || !activeConvId) return;

        // Unsubscribe from old
        if (subRefs.current[activeConvId]) {
            subRefs.current[activeConvId].unsubscribe();
        }

        let sub;
        subscribe(`/topic/conversation.${activeConvId}`, (newMsg) => {
            setMessages(prev => ({
                ...prev,
                [activeConvId]: [...(prev[activeConvId] || []), newMsg],
            }));
            // Cập nhật lastMessage trong conversation list
            setConversations(prev =>
                prev.map(c => c.id === activeConvId
                    ? { ...c, lastMessage: newMsg, updatedAt: newMsg.createdAt }
                    : c
                ).sort((a, b) => new Date(b.updatedAt || 0) - new Date(a.updatedAt || 0))
            );
        }).then(s => {
            sub = s;
            subRefs.current[activeConvId] = s;
        });

        return () => { sub?.unsubscribe(); };
    }, [connected, activeConvId, subscribe]);

    //Load messages for a conversation 
    const loadMessages = useCallback(async (convId, page = 0) => {
        setLoadingMsgs(true);
        try {
            const res = await chatService.getMessages(convId, page, 30);
            const data = res.data; // Spring Page object
            const msgs = [...data.content].reverse(); // API trả newest-first, cần đảo lại

            setMessages(prev => ({
                ...prev,
                [convId]: page === 0 ? msgs : [...msgs, ...(prev[convId] || [])],
            }));
            setPageInfo(prev => ({
                ...prev,
                [convId]: { page, hasMore: !data.last },
            }));
        } catch (e) {
            console.error(e);
        } finally {
            setLoadingMsgs(false);
        }
    }, []);

    //Select a conversation 
    const selectConversation = useCallback((convId) => {
        setActiveConvId(convId);
        if (!messages[convId]) {
            loadMessages(convId, 0);
        }
    }, [messages, loadMessages]);

    //Load older messages 
    const loadMore = useCallback(() => {
        if (!activeConvId) return;
        const info = pageInfo[activeConvId];
        if (!info?.hasMore || loadingMsgs) return;
        loadMessages(activeConvId, info.page + 1);
    }, [activeConvId, pageInfo, loadingMsgs, loadMessages]);

    //Send message via WebSocket 
    const sendMessage = useCallback((content) => {
        if (!activeConvId || !content.trim()) return;
        publish('/app/chat.sendMessage', {
            conversationId: activeConvId,
            content: content.trim(),
            type: 'TEXT',
        });
    }, [activeConvId, publish]);

    //Create direct chat from FollowPage
    const openDirectChat = useCallback(async (targetUserId) => {
        try {
            const res = await chatService.createOrGetDirectChat(targetUserId);
            const conv = res.data;
            setConversations(prev => {
                const exists = prev.find(c => c.id === conv.id);
                if (exists) return prev;
                return [conv, ...prev];
            });
            setActiveConvId(conv.id);
            if (!messages[conv.id]) loadMessages(conv.id, 0);
            return conv;
        } catch (e) {
            console.error(e);
            throw e;
        }
    }, [messages, loadMessages]);

    const createGroup = useCallback(async (title, memberIds) => {
        const res = await chatService.createGroup(title, memberIds);
        const conv = res.data;
        setActiveConvId(conv.id);
        return conv;
    }, []);


    const addMember = useCallback(async (convId, userId) => {
        await chatService.addMember(convId, userId);
        // Refresh conversation to get updated participants list
        const res = await chatService.getConversations();
        setConversations(res.data);
    }, []);


    const leaveGroup = useCallback(async (convId) => {
        await chatService.leaveGroup(convId);
        setConversations(prev => prev.filter(c => c.id !== convId));
        if (activeConvId === convId) setActiveConvId(null);
    }, [activeConvId]);

    const activeConversation = conversations.find(c => c.id === activeConvId) || null;
    const activeMessages = messages[activeConvId] || [];
    const activePageInfo = pageInfo[activeConvId] || { page: 0, hasMore: false };

    return (
        <ChatContext.Provider value={{
            // state
            currentUser,
            connected,
            conversations,
            activeConvId,
            activeConversation,
            activeMessages,
            loadingConvs,
            loadingMsgs,
            activePageInfo,
            // actions
            selectConversation,
            sendMessage,
            loadMore,
            addMember,
            openDirectChat,
            createGroup,
            leaveGroup,
        }}>
            {children}
        </ChatContext.Provider>
    );
}

export function useChatContext() {
    const ctx = useContext(ChatContext);
    if (!ctx) throw new Error('useChatContext must be inside ChatProvider');
    return ctx;
}