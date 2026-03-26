// src/features/user/components/Chat/components/CreateGroupModal.jsx
import { useState, useEffect, useCallback, useRef } from 'react';
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

function UserRow({ user, checked, onToggle }) {
    return (
        <label className="ugm-user-row">
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
            <input
                type="checkbox"
                checked={checked}
                onChange={() => onToggle(user)}
                className="ugm-checkbox"
            />
        </label>
    );
}

export default function CreateGroupModal({ onClose }) {
    const { createGroup, selectConversation } = useChatContext();
    const [title, setTitle] = useState('');
    const [search, setSearch] = useState('');
    const [results, setResults] = useState([]);
    const [selected, setSelected] = useState([]);
    const [searching, setSearching] = useState(false);
    const [creating, setCreating] = useState(false);
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


    const toggle = (user) => {
        setSelected(prev =>
            prev.find(u => u.id === user.id)
                ? prev.filter(u => u.id !== user.id)
                : [...prev, user]
        );
    };

    const handleCreate = async () => {
        setError('');
        if (!title.trim()) { setError('Vui lòng nhập tên nhóm'); return; }
        if (selected.length < 1) { setError('Chọn ít nhất 1 thành viên'); return; }
        setCreating(true);
        try {
            const conv = await createGroup(title.trim(), selected.map(u => u.id));
            selectConversation(conv.id);
            onClose();
        } catch (e) {
            const msg =
                e.response?.data?.data?.[0]?.message ||
                e.response?.data?.message ||
                e.message ||
                'Tạo nhóm thất bại';

            setError(msg);
        } finally {
            setCreating(false);
        }
    };

    return (
        <div className="ugm-backdrop" >
            <div className="ugm-modal">
                <div className="ugm-header">
                    <h3>Tạo nhóm chat</h3>
                    <button className="ugm-close" onClick={onClose}>×</button>
                </div>

                <div className="ugm-body">
                    {/* Tên nhóm */}
                    <div className="ugm-field">
                        <label>Tên nhóm</label>
                        <input
                            className="ugm-input"
                            placeholder="Nhập tên nhóm..."
                            value={title}
                            onChange={e => setTitle(e.target.value)}
                            autoFocus
                        />
                    </div>

                    {/* Selected chips */}
                    {selected.length > 0 && (
                        <div className="ugm-chips">
                            {selected.map(u => (
                                <span key={u.id} className="ugm-chip">
                                    {u.fullName || u.username}
                                    <button onClick={() => toggle(u)}>×</button>
                                </span>
                            ))}
                        </div>
                    )}

                    {/* Search users */}
                    <div className="ugm-field">
                        <label>Thêm thành viên ({selected.length} đã chọn)</label>
                        <input
                            className="ugm-input"
                            placeholder="Tìm theo username..."
                            value={search}
                            onChange={e => setSearch(e.target.value)}
                        />
                    </div>

                    <div className="ugm-user-list">
                        {searching && <p className="ugm-hint">Đang tìm...</p>}
                        {!searching && debouncedSearch && results.length === 0 && (
                            <p className="ugm-hint">Không tìm thấy người dùng</p>
                        )}
                        {results.map(user => (
                            <UserRow
                                key={user.id}
                                user={user}
                                checked={!!selected.find(u => u.id === user.id)}
                                onToggle={toggle}
                            />
                        ))}
                    </div>

                    {error && <p className="ugm-error">{error}</p>}
                </div>

                <div className="ugm-footer">
                    <button className="ugm-btn-cancel" onClick={onClose}>Hủy</button>
                    <button
                        className="ugm-btn-create"
                        onClick={handleCreate}
                        disabled={creating || !title.trim() || selected.length === 0}
                    >
                        {creating ? 'Đang tạo...' : `Tạo nhóm (${selected.length + 1} người)`}
                    </button>
                </div>
            </div>
        </div>
    );
}