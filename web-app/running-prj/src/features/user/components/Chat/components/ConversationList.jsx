// src/features/user/components/Chat/components/ConversationList.jsx
import { useState } from 'react';
import { useChatContext } from '../context/ChatContext';
import CreateGroupModal from './CreateGroupModal.jsx';
import CreateDirectChatModal from './CreateDirectChatModal.jsx';
import './ConversationList.css';

function Avatar({ name, imageUrl, size = 42 }) {
  const COLORS = ['#e67e22', '#3498db', '#9b59b6', '#1abc9c', '#e74c3c', '#f39c12', '#16a085'];
  const color = COLORS[(name?.charCodeAt(0) || 0) % COLORS.length];
  const initials = (name || '?').split(' ').map(w => w[0]).slice(0, 2).join('').toUpperCase();

  return (
    <div className="avatar-wrap" style={{ width: size, height: size }}>
      {imageUrl
        ? <img src={imageUrl} alt={name} className="avatar-img" />
        : <div className="avatar-placeholder" style={{ background: color, fontSize: size * 0.37 }}>{initials}</div>
      }

    </div>
  );
}

function ConvItem({ conv, active, onClick, currentUserId }) {

  const getAvatar = (conv) => {
    if (!conv.participants) return null;

    const other = conv.participants.find(
      p => p.userId !== currentUserId && p.imageUrl
    );

    return other?.imageUrl;
  };

  const lastMsg = conv.lastMessage;
  const time = conv.updatedAt
    ? new Date(conv.updatedAt).toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })
    : '';

  const preview = lastMsg
    ? `${lastMsg.senderName?.split(' ').pop()}: ${lastMsg.content}`
    : 'Bắt đầu trò chuyện...';

  return (
    <button className={`conv-item ${active ? 'conv-item--active' : ''}`} onClick={onClick}>

      <Avatar
        name={conv.title}
        imageUrl={getAvatar(conv)}
        size={46}
      />

      <div className="conv-item__body">
        <div className="conv-item__top">
          <span className="conv-item__title">{conv.title}</span>
          {time && <span className="conv-item__time">{time}</span>}
        </div>
        <div className="conv-item__bottom">
          {conv.isGroup && <span className="conv-item__badge">Nhóm</span>}
          <span className="conv-item__preview">{preview}</span>
        </div>
      </div>
    </button>
  );
}

export default function ConversationList() {
  const { conversations, activeConvId, selectConversation, loadingConvs, connected, currentUser } = useChatContext(); const [search, setSearch] = useState('');
  const [showGroupModal, setShowGroupModal] = useState(false);
  const [showDirectModal, setShowDirectModal] = useState(false);

  const filtered = conversations.filter(c =>
    c.title?.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <>
      <aside className="conv-list-sidebar">
        {/* Header */}
        <div className="conv-list-header">
          <div className="conv-list-header__top">
            <h2 className="conv-list-title">Tin nhắn</h2>
            <span className={`ws-badge ${connected ? 'ws-badge--on' : 'ws-badge--off'}`}>
              {connected ? '● Online' : '○ Đang kết nối'}
            </span>
          </div>

          {/* Search */}
          <div className="conv-search">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <circle cx="11" cy="11" r="8" /><line x1="21" y1="21" x2="16.65" y2="16.65" />
            </svg>
            <input
              placeholder="Tìm cuộc trò chuyện..."
              value={search}
              onChange={e => setSearch(e.target.value)}
            />
          </div>

          {/* Action buttons */}
          <div className="conv-list-actions">
            <button className="btn-create-direct" onClick={() => setShowDirectModal(true)} title="Nhắn tin trực tiếp">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                <circle cx="12" cy="7" r="4" />
                <line x1="19" y1="11" x2="19" y2="17" />
                <line x1="16" y1="14" x2="22" y2="14" />
              </svg>
              Nhắn tin
            </button>
            <button className="btn-create-group" onClick={() => setShowGroupModal(true)} title="Tạo nhóm">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
                <circle cx="9" cy="7" r="4" />
                <path d="M23 21v-2a4 4 0 0 0-3-3.87" />
                <path d="M16 3.13a4 4 0 0 1 0 7.75" />
              </svg>
              Tạo nhóm
            </button>
          </div>
        </div>

        {/* List */}
        <div className="conv-list-body">
          {loadingConvs
            ? Array(5).fill(0).map((_, i) => <div key={i} className="conv-skeleton" />)
            : filtered.length === 0
              ? (
                <div className="conv-empty">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
                    <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z" />
                  </svg>
                  <p>{search ? 'Không tìm thấy' : 'Chưa có cuộc trò chuyện'}</p>
                </div>
              )
              : filtered.map(conv => (
                <ConvItem
                  key={conv.id}
                  conv={conv}
                  active={conv.id === activeConvId}
                  onClick={() => selectConversation(conv.id)}
                  currentUserId={currentUser?.id}

                />
              ))
          }
        </div>
      </aside>

      {showGroupModal && (
        <CreateGroupModal onClose={() => setShowGroupModal(false)} />
      )}
      {showDirectModal && (
        <CreateDirectChatModal onClose={() => setShowDirectModal(false)} />
      )}
    </>
  );
}