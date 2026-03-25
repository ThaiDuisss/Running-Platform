// src/features/user/components/Chat/components/Chat.jsx
import { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { ChatProvider, useChatContext } from '../context/ChatContext';
import ConversationList from './ConversationList';
import ChatWindow from './ChatWindow';
import './Chat.css';

// Inner component có access to context
function ChatInner() {
  const { openDirectChat } = useChatContext();
  const location = useLocation();

  // Xử lý navigate từ FollowPage
  useEffect(() => {
    const targetUserId = location.state?.targetUserId;
    if (targetUserId) {
      openDirectChat(targetUserId);
      // Clear state để tránh re-open khi quay lại trang
      window.history.replaceState({}, '');
    }
  }, [location.state?.targetUserId]);

  return (
    <div className="chat-layout">
      <ConversationList />
      <ChatWindow />
    </div>
  );
}

export default function Chat() {
  return (
    <ChatProvider>
      <ChatInner />
    </ChatProvider>
  );
}