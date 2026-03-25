// src/features/user/components/Chat/components/ChatWindow.jsx
import { useState, useEffect, useRef, useCallback } from 'react';
import { useChatContext } from '../context/ChatContext';
import GroupInfoModal from './GroupInfoModal.jsx';
import './ChatWindow.css';

function Avatar({ name, imageUrl, size = 34 }) {
  const COLORS = ['#e67e22', '#3498db', '#9b59b6', '#1abc9c', '#e74c3c', '#f39c12'];
  const color = COLORS[(name?.charCodeAt(0) || 0) % COLORS.length];
  const initials = (name || '?').split(' ').map(w => w[0]).slice(0, 2).join('').toUpperCase();
  return (
    <div style={{
      width: size, height: size, borderRadius: '50%', flexShrink: 0,
      background: imageUrl ? 'transparent' : color,
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      color: '#fff', fontWeight: 700, fontSize: size * 0.37, overflow: 'hidden',
    }}>
      {imageUrl ? <img src={imageUrl} alt={name} style={{ width: '100%', height: '100%', objectFit: 'cover' }} /> : initials}
    </div>
  );
}

function MessageBubble({ msg, isOwn, showAvatar, prevSame }) {
  const time = msg.createdAt
    ? new Date(msg.createdAt).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })
    : '';

  return (
    <div className={`msg-row ${isOwn ? 'msg-row--own' : 'msg-row--other'}`}
      style={{ marginTop: prevSame ? 2 : 14 }}>
      {!isOwn && (
        <div className="msg-avatar-slot">
          {showAvatar && <Avatar name={msg.senderName} imageUrl={msg.senderAvatar} size={32} />}
        </div>
      )}
      <div className="msg-body">
        {!isOwn && !prevSame && <span className="msg-sender">{msg.senderName}</span>}
        <div className={`msg-bubble ${isOwn ? 'msg-bubble--own' : 'msg-bubble--other'}`}>
          {msg.content}
        </div>
        <span className="msg-time">{time}</span>
      </div>
    </div>
  );
}

export default function ChatWindow() {
  const {
    currentUser,
    activeConversation,
    activeMessages,
    loadingMsgs,
    activePageInfo,
    sendMessage,
    loadMore,
    leaveGroup,
  } = useChatContext();

  const [input, setInput] = useState('');
  const [showGroupInfo, setShowGroupInfo] = useState(false);
  const bottomRef = useRef(null);
  const inputRef = useRef(null);
  const isFirstLoad = useRef(true);

  // Scroll to bottom khi có tin nhắn mới (không scroll khi load more cũ)
  useEffect(() => {
    if (isFirstLoad.current) {
      bottomRef.current?.scrollIntoView({ behavior: 'auto' });
      isFirstLoad.current = false;
    } else {
      const last = activeMessages[activeMessages.length - 1];
      // Chỉ auto-scroll nếu tin nhắn mới nhất là của mình hoặc đang ở gần cuối
      if (last?.senderId === currentUser?.id) {
        bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
      }
    }
  }, [activeMessages.length]);

  // Reset khi đổi conversation
  useEffect(() => {
    isFirstLoad.current = true;
    setInput('');
  }, [activeConversation?.id]);

  const handleSend = useCallback(() => {
    if (!input.trim()) return;
    sendMessage(input);
    setInput('');
    inputRef.current?.focus();
  }, [input, sendMessage]);

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };



  if (!activeConversation) {
    return (
      <div className="chat-window chat-window--empty">
        <div className="chat-empty-state">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.2">
            <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
          </svg>
          <h3>Chọn cuộc trò chuyện</h3>
          <p>Hoặc nhắn tin với ai đó từ trang Follow</p>
        </div>
      </div>
    );
  }

  return (
    <div className="chat-window">
      {/* ── Group Info Modal ── */}
      {showGroupInfo && activeConversation.isGroup && (
        <GroupInfoModal onClose={() => setShowGroupInfo(false)} />
      )}

      {/* ── Header ── */}
      <div className="cw-header">
        <div className="cw-header__info">
          <div
            className={`cw-avatar${activeConversation.isGroup ? ' cw-avatar--clickable' : ''}`}
            onClick={() => activeConversation.isGroup && setShowGroupInfo(true)}
            title={activeConversation.isGroup ? 'Xem thành viên nhóm' : undefined}
          >
            {activeConversation.title?.split('')[0]?.toUpperCase()}
          </div>
          <div>
            <div className="cw-header__title">{activeConversation.title}</div>
            <div className="cw-header__sub">
              {activeConversation.isGroup
                ? <button className="cw-members-btn" onClick={() => setShowGroupInfo(true)}>{activeConversation.participants?.length || 0} thành viên</button>
                : 'Trò chuyện trực tiếp'}
            </div>
          </div>
        </div>
      </div>

      {/* ── Messages ── */}
      <div className="cw-messages">
        {activePageInfo.hasMore && (
          <button className="btn-load-more" onClick={loadMore} disabled={loadingMsgs}>
            {loadingMsgs ? 'Đang tải...' : 'Xem tin nhắn cũ hơn'}
          </button>
        )}

        {activeMessages.map((msg, idx) => {
          const isOwn = msg.senderId === currentUser?.id;
          const prev = activeMessages[idx - 1];
          const next = activeMessages[idx + 1];
          const prevSame = prev?.senderId === msg.senderId;
          const nextSame = next?.senderId === msg.senderId;

          return (
            <MessageBubble
              key={msg.id ?? `temp-${idx}`}
              msg={msg}
              isOwn={isOwn}
              showAvatar={!nextSame}
              prevSame={prevSame}
            />
          );
        })}
        <div ref={bottomRef} />
      </div>

      {/* ── Input ── */}
      <div className="cw-input-area">
        <textarea
          ref={inputRef}
          className="cw-input"
          placeholder="Nhập tin nhắn... (Enter để gửi)"
          value={input}
          onChange={e => setInput(e.target.value)}
          onKeyDown={handleKeyDown}
          rows={1}
        />
        <button
          className={`cw-send-btn ${input.trim() ? 'cw-send-btn--active' : ''}`}
          onClick={handleSend}
          disabled={!input.trim()}
        >
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <line x1="22" y1="2" x2="11" y2="13" />
            <polygon points="22 2 15 22 11 13 2 9 22 2" />
          </svg>
        </button>
      </div>
    </div>
  );
}