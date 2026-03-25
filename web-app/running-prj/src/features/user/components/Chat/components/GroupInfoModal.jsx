// src/features/user/components/Chat/components/GroupInfoModal.jsx
import { useState, useEffect, useCallback } from 'react';
import { useChatContext } from '../context/ChatContext';
import { chatService } from '../services/chatService';
import './GroupInfoModal.css';

function useDebounce(value, delay) {
    const [deb, setDeb] = useState(value);
    useEffect(() => {
        const t = setTimeout(() => setDeb(value), delay);
        return () => clearTimeout(t);
    }, [value, delay]);
    return deb;
}

function MemberAvatar({ name, imageUrl, size = 40 }) {
    const COLORS = ['#e67e22', '#3498db', '#9b59b6', '#1abc9c', '#e74c3c', '#f39c12', '#16a085'];
    const color = COLORS[(name?.charCodeAt(0) || 0) % COLORS.length];
    const initials = (name || '?').split(' ').map(w => w[0]).slice(0, 2).join('').toUpperCase();
    return (
        <div className="gim-member-avatar" style={{ width: size, height: size, background: imageUrl ? 'transparent' : color }}>
            {imageUrl ? <img src={imageUrl} alt={name} /> : <span style={{ fontSize: size * 0.37 }}>{initials}</span>}
        </div>
    );
}

export default function GroupInfoModal({ onClose }) {
    const { activeConversation, leaveGroup, addMember, currentUser } = useChatContext();

    // Leave group
    const [leaving, setLeaving] = useState(false);
    const [showConfirm, setShowConfirm] = useState(false);

    // Add member 
    const [search, setSearch] = useState('');
    const [searchResult, setSearchResult] = useState(null);
    const [searching, setSearching] = useState(false);
    const [adding, setAdding] = useState(false);
    const [addError, setAddError] = useState('');
    const [addSuccess, setAddSuccess] = useState('');

    const debouncedSearch = useDebounce(search, 350);

    useEffect(() => {
        setSearchResult(null);
        setAddError('');
        setAddSuccess('');
        if (!debouncedSearch.trim()) return;

        setSearching(true);
        chatService.searchUsers(debouncedSearch)
            .then(res => setSearchResult(res.data?.data ?? null))
            .catch(() => setSearchResult(null))
            .finally(() => setSearching(false));
    }, [debouncedSearch]);

    const isAlreadyMember = useCallback((userId) => {
        return activeConversation?.participants?.some(p => p.userId === userId);
    }, [activeConversation]);

    const handleAdd = async (user) => {
        setAddError('');
        setAddSuccess('');
        setAdding(true);
        try {
            await addMember(activeConversation.id, user.id);
            setAddSuccess(`Đã thêm ${user.fullName || user.username} vào nhóm!`);
            setSearch('');
            setSearchResult(null);
        } catch (e) {
            setAddError(e.response?.data?.message || e.message || 'Thêm thành viên thất bại');
        } finally {
            setAdding(false);
        }
    };

    const handleLeave = async () => {
        setLeaving(true);
        try {
            await leaveGroup(activeConversation.id);
            onClose();
        } catch (e) {
            alert(e.response?.data?.message || e.message || 'Rời nhóm thất bại');
        } finally {
            setLeaving(false);
            setShowConfirm(false);
        }
    };

    if (!activeConversation) return null;
    const { title, participants = [] } = activeConversation;

    return (
        <>
            <div className="gim-backdrop" onClick={e => e.target === e.currentTarget && onClose()}>
                <div className="gim-modal">

                    <div className="gim-header">
                        <div className="gim-group-avatar">{title?.split('')[0]?.toUpperCase()}</div>
                        <div className="gim-header-info">
                            <h3>{title}</h3>
                            <span>{participants.length} thành viên</span>
                        </div>
                        <button className="gim-close" onClick={onClose}>×</button>
                    </div>

                    <div className="gim-body">

                        {/* Add member */}
                        <p className="gim-section-label">Thêm thành viên</p>
                        <div className="gim-add-wrap">
                            <div className="gim-search-row">
                                <svg className="gim-search-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                                    <circle cx="11" cy="11" r="8" /><line x1="21" y1="21" x2="16.65" y2="16.65" />
                                </svg>
                                <input
                                    className="gim-search-input"
                                    placeholder="Tìm theo tên, username..."
                                    value={search}
                                    onChange={e => { setSearch(e.target.value); setAddError(''); setAddSuccess(''); }}
                                />
                                {search && (
                                    <button className="gim-search-clear" onClick={() => { setSearch(''); setSearchResult(null); }}>×</button>
                                )}
                            </div>

                            {/* Search result */}
                            {searching && <p className="gim-hint">Đang tìm...</p>}
                            {!searching && search && !searchResult && <p className="gim-hint">Không tìm thấy người dùng</p>}
                            {searchResult && (
                                <div className="gim-search-result">
                                    <MemberAvatar name={searchResult.fullName || searchResult.username} imageUrl={searchResult.imageUrl} size={38} />
                                    <div className="gim-sr-info">
                                        <span className="gim-sr-name">{searchResult.fullName || searchResult.username}</span>
                                        <span className="gim-sr-username">@{searchResult.username}</span>
                                    </div>
                                    {isAlreadyMember(searchResult.id)
                                        ? <span className="gim-badge gim-badge--already">Đã có</span>
                                        : (
                                            <button
                                                className="gim-btn-add"
                                                onClick={() => handleAdd(searchResult)}
                                                disabled={adding}
                                            >
                                                {adding ? '...' : '+ Thêm'}
                                            </button>
                                        )
                                    }
                                </div>
                            )}

                            {addSuccess && <p className="gim-success">{addSuccess}</p>}
                            {addError && <p className="gim-error">{addError}</p>}
                        </div>

                        {/* Member list */}
                        <p className="gim-section-label" style={{ marginTop: 18 }}>Thành viên</p>
                        <div className="gim-member-list">
                            {participants.map(p => (
                                <div key={p.userId} className="gim-member-row">
                                    <MemberAvatar name={p.fullName || p.username} imageUrl={p.imageUrl} />
                                    <div className="gim-member-info">
                                        <span className="gim-member-name">
                                            {p.fullName || p.username}
                                            {p.userId === currentUser?.id && (
                                                <span className="gim-badge gim-badge--you">Bạn</span>
                                            )}
                                            {p.isAdmin && (
                                                <span className="gim-badge gim-badge--admin">Admin</span>
                                            )}
                                        </span>
                                        <span className="gim-member-username">@{p.username}</span>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>

                    {/*  Footer  */}
                    <div className="gim-footer">
                        <button className="gim-btn-leave" onClick={() => setShowConfirm(true)} disabled={leaving}>
                            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                                <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
                                <polyline points="16 17 21 12 16 7" />
                                <line x1="21" y1="12" x2="9" y2="12" />
                            </svg>
                            Rời nhóm
                        </button>
                    </div>
                </div>
            </div>

            {/*  Confirm Leave Modal  */}
            {showConfirm && (
                <div className="confirm-backdrop">
                    <div className="confirm-modal">
                        <h3>Rời nhóm?</h3>
                        <p>Bạn sẽ không còn nhận tin nhắn từ nhóm này nữa.</p>
                        <div className="confirm-actions">
                            <button className="btn-cancel" onClick={() => setShowConfirm(false)} disabled={leaving}>Hủy</button>
                            <button className="btn-danger" onClick={handleLeave} disabled={leaving}>
                                {leaving ? 'Đang rời...' : 'Rời nhóm'}
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}