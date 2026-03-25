// src/features/user/components/Chat/components/CreateDirectChatModal.jsx
import { useState, useEffect } from 'react';
import { chatService } from '../services/chatService';
import { useChatContext } from '../context/ChatContext';
import './CreateGroupModal.css';

function useDebounce(value, delay) {
    const [debounced, setDebounced] = useState(value);
    useEffect(() => {
        const t = setTimeout(() => setDebounced(value), delay);
        return () => clearTimeout(t);
    }, [value, delay]);
    return debounced;
}

function UserRow({ user, onClick }) {
    return (
        <button className="ugm-user-row dcm-user-row" onClick={() => onClick(user)}>
            <div className="ugm-user-avatar">
                {user.imageUrl
                    ? <img src={user.imageUrl} alt={user.username} />
                    : <span>{(user.fullName || user.username || '?')[0].toUpperCase()}</span>
                }
            </div>
            <div className="ugm-user-info">
                <strong>{user.fullName || user.username}</strong>
                <span>@{user.username}</span>
            </div>
            <svg className="dcm-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="5" y1="12" x2="19" y2="12" />
                <polyline points="12 5 19 12 12 19" />
            </svg>
        </button>
    );
}

export default function CreateDirectChatModal({ onClose }) {
    const { openDirectChat } = useChatContext();
    const [search, setSearch] = useState('');
    const [results, setResults] = useState([]);
    const [searching, setSearching] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const debouncedSearch = useDebounce(search, 350);

    useEffect(() => {
        if (!debouncedSearch.trim()) { setResults([]); return; }
        setSearching(true);
        chatService.searchUsers(debouncedSearch)
            .then(res => setResults(res.data.data.content || []))
            .catch(console.error)
            .finally(() => setSearching(false));
    }, [debouncedSearch]);


    const handleSelect = async (user) => {
        setError('');
        setLoading(true);
        try {
            await openDirectChat(user.id);
            onClose();
        } catch (e) {
            setError(e.response?.data?.message || e.message || 'Không thể mở cuộc trò chuyện');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="ugm-backdrop" onClick={e => e.target === e.currentTarget && onClose()}>
            <div className="ugm-modal">
                <div className="ugm-header">
                    <h3>Nhắn tin trực tiếp</h3>
                    <button className="ugm-close" onClick={onClose}>×</button>
                </div>

                <div className="ugm-body">
                    <div className="ugm-field">
                        <label>Tìm người dùng</label>
                        <input
                            className="ugm-input"
                            placeholder="Tìm theo tên hoặc username..."
                            value={search}
                            onChange={e => setSearch(e.target.value)}
                            autoFocus
                        />
                    </div>

                    <div className="ugm-user-list">
                        {searching && <p className="ugm-hint">Đang tìm...</p>}
                        {!searching && debouncedSearch && results.length === 0 && (
                            <p className="ugm-hint">Không tìm thấy người dùng</p>
                        )}
                        {!debouncedSearch && (
                            <p className="ugm-hint">Nhập tên hoặc username để tìm kiếm</p>
                        )}
                        {results.map(user => (
                            <UserRow
                                key={user.id}
                                user={user}
                                onClick={handleSelect}
                            />
                        ))}
                    </div>

                    {error && <p className="ugm-error">{error}</p>}
                    {loading && <p className="ugm-hint">Đang mở cuộc trò chuyện...</p>}
                </div>

                <div className="ugm-footer">
                    <button className="ugm-btn-cancel" onClick={onClose}>Đóng</button>
                </div>
            </div>
        </div>
    );
}
