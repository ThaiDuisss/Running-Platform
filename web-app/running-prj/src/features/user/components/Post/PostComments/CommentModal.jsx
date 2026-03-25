import React, { useEffect, useState, useContext, useRef } from "react";
import { Modal } from "react-bootstrap";
import CommentItem from "./CommentItem";
import { AuthDataContext } from "@/app/providers/AuthProvider";
import {
    getCommentsByPostId,
    createComment,
    reactPost,
    countReplyPost,
    countCommentReply
} from "@/features/admin/users/services/UserService";
import "@/features/user/components/Post/Feed.css";

const REACTION_TYPES = [
    { type: "LIKE", icon: "👍", label: "Thích", color: "#2078f4" },
    { type: "LOVE", icon: "❤️", label: "Yêu thích", color: "#f02849" },
    { type: "HAHA", icon: "😂", label: "Haha", color: "#f7b125" },
    { type: "WOW", icon: "😮", label: "Wow", color: "#f7b125" },
    { type: "SAD", icon: "😢", label: "Buồn", color: "#f7b125" },
    { type: "ANGRY", icon: "😡", label: "Phẫn nộ", color: "#e9710f" }
];

export default function CommentModal({ show, onClose, post }) {
    const { user } = useContext(AuthDataContext);
    const [comments, setComments] = useState([]);
    const [content, setContent] = useState("");
    const [loading, setLoading] = useState(false);
    const [reaction, setReaction] = useState(null);
    const [showOptions, setShowOptions] = useState(false);
    const [countReply, setCountReply] = useState(0);
    const hideTimeout = useRef(null);
    const [countCommentReacts, setCountCommentReacts] = useState(0);

    const commentInputRef = useRef(null);

    const fetchComments = async () => {
        try {
            const res = await getCommentsByPostId(post.id);
            setComments(res.data.data || []);
        } catch (err) {
            console.error(err);
            setComments([]);
        }
    };

    const fetchCountReply = async () => {
        try {
            const res = await countReplyPost(post.id);
            setCountReply(res.data.data);
        } catch (error) {
            console.error("Failed to fetch reply count:", error);
        }
    };

    const fetchCountCommentReacts = async () => {
        try {
            const res = await countCommentReply(post.id);
            setCountCommentReacts(res.data.data);
        } catch (error) {
            console.error("Failed to fetch comment reaction count:", error);
        }
    };


    useEffect(() => {
        if (show && post?.id) {
            fetchComments();
            fetchCountReply();
            fetchCountCommentReacts();

            const userKey = user?.id || user?.userId || user?.username || "guest";
            const localStorageKey = `post-reaction-${post.id}-${userKey}`;
            const storedReaction = localStorage.getItem(localStorageKey);
            setReaction(post.reaction || post.reactionType || post.userReaction || storedReaction || null);
        }
    }, [show, post?.id, user]);

    const handleSubmit = async () => {
        if (!content.trim()) return;
        try {
            setLoading(true);
            await createComment({
                postId: post.id,
                content,
                parentCommentId: null
            });
            setContent("");
            await fetchComments();
            await fetchCountCommentReacts();
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleReact = async (postId, reactionType) => {
        try {
            const res = await reactPost(postId, reactionType);
            const data = res.data.data;
            const serverReaction = data?.reaction || data?.reactionType || null;

            const userKey = user?.id || user?.userId || user?.username || "guest";
            const localStorageKey = `post-reaction-${postId}-${userKey}`;

            setReaction(serverReaction);
            try {
                if (serverReaction) {
                    localStorage.setItem(localStorageKey, serverReaction);
                } else {
                    localStorage.removeItem(localStorageKey);
                }
            } catch (e) { }

            await fetchCountReply();
            setShowOptions(false);
        } catch (error) {
            console.error("Failed to react:", error);
        }
    };

    const handleMouseEnter = () => {
        clearTimeout(hideTimeout.current);
        setShowOptions(true);
    };

    const handleMouseLeave = () => {
        hideTimeout.current = setTimeout(() => setShowOptions(false), 300);
    };

    const currentReactionConfig = REACTION_TYPES.find(r => r.type === reaction);

    return (
        <Modal show={show} onHide={onClose} centered size="lg" scrollable>

            {/* HEADER */}
            <div className="modal-header">
                <h5 className="modal-title" style={{ width: "100%", textAlign: "center", fontWeight: 700, fontSize: 18 }}>
                    Bài viết của {post?.username || 'Người dùng'}
                </h5>
                <button className="btn-close" onClick={onClose} style={{ position: "absolute", right: 16 }}></button>
            </div>

            {/* BODY */}
            <div className="modal-body comment-modal-body">

                {/* ---BÀI VIẾT --- */}
                {post && (
                    <div style={{ paddingBottom: 16, marginBottom: 16, borderBottom: "1px solid var(--border-color, #e4e6eb)" }}>
                        <div className="post-header" style={{ marginBottom: 12 }}>
                            <img src={user?.imageUrl} alt="avatar" />
                            <div>
                                <div className="post-author">{post?.username || 'Người dùng ẩn danh'}</div>
                                {/* <div className="post-time" style={{ fontSize: 13, color: "var(--text-secondary)" }}>
                                    Vừa xong · 🌍
                                </div> */}
                            </div>
                        </div>

                        {post?.content && (
                            <div className="post-content-text" style={{ fontSize: 15, marginBottom: 12 }}>
                                {post.content}
                            </div>
                        )}

                        {post?.images && post.images.length > 0 && (
                            <div
                                className="post-images"
                                style={{
                                    display: "grid", gap: 4, gridTemplateColumns: post.images.length === 1 ? "1fr" : "1fr 1fr",
                                    margin: "0 0 16px 0", borderRadius: 8, overflow: "hidden"
                                }}
                            >
                                {post.images.map((img, index) => (
                                    <img key={index} src={img} alt="post media" style={{ width: "100%", height: "100%", objectFit: "cover", maxHeight: post.images.length === 1 ? 400 : 300 }} />
                                ))}
                            </div>
                        )}

                        <div className="post-stats">
                            <div>
                                <span style={{ fontSize: '18px', marginRight: '4px' }}>
                                    {currentReactionConfig ? currentReactionConfig.icon : "👍"}
                                </span>
                                {countReply} người đã bày tỏ cảm xúc
                            </div>
                            <div>{countCommentReacts} Bình luận</div>
                        </div>

                        <div className="post-actions" style={{ marginTop: "4px" }}>

                            {/*Thích */}
                            <div
                                className="action-btn"
                                onMouseEnter={handleMouseEnter}
                                onMouseLeave={handleMouseLeave}
                                style={{ position: 'relative' }}
                            >
                                <div
                                    style={{
                                        display: 'flex', alignItems: 'center', gap: '6px',
                                        color: currentReactionConfig ? currentReactionConfig.color : 'inherit',
                                        fontWeight: currentReactionConfig ? 'bold' : 'normal'
                                    }}
                                    onClick={() => handleReact(post.id, "LIKE")}
                                >
                                    <span style={{ fontSize: '20px' }}>
                                        {currentReactionConfig ? currentReactionConfig.icon : "👍"}
                                    </span>
                                    {currentReactionConfig ? currentReactionConfig.label : "Thích"}
                                </div>

                                {/* Popup Reaction */}
                                {showOptions && (
                                    <div className="reaction-picker" style={{ bottom: '100%', left: '0', marginBottom: '10px' }}>
                                        {REACTION_TYPES.map(r => (
                                            <span
                                                key={r.type}
                                                className="reaction-icon"
                                                title={r.label}
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    handleReact(post.id, r.type);
                                                }}
                                            >
                                                {r.icon}
                                            </span>
                                        ))}
                                    </div>
                                )}
                            </div>

                            {/* Nút Bình luận - Click vào sẽ focus xuống ô input */}
                            <div
                                className="action-btn"
                                onClick={() => {
                                    if (commentInputRef.current) {
                                        commentInputRef.current.focus();
                                    }
                                }}
                            >
                                💬 Bình luận
                            </div>

                            {/* Nút Chia sẻ */}
                            {/* <div className="action-btn">
                                ↗ Chia sẻ
                            </div> */}
                        </div>
                    </div>
                )}
                {/* --- KẾT THÚC CHI TIẾT BÀI VIẾT --- */}

                {/* --- DANH SÁCH BÌNH LUẬN --- */}
                {(!comments || comments.length === 0) ? (
                    <div style={{ textAlign: "center", color: "var(--text-secondary)", padding: "20px 0" }}>
                        Chưa có bình luận nào. Hãy là người đầu tiên!
                    </div>
                ) : (
                    comments.map((c, index) => (
                        <CommentItem
                            key={c?.id || index}
                            comment={c}
                            postId={post?.id}
                            refresh={fetchComments}
                        />
                    ))
                )}
            </div>

            {/* FOOTER: Ô NHẬP COMMENT */}
            <div className="modal-footer comment-footer">
                <img src={user?.avatar} alt="my-avatar" className="comment-avatar" />
                <div className="comment-input-box">
                    <input
                        ref={commentInputRef} // Gắn ref vào đây để auto-focus
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        onKeyDown={(e) => e.key === 'Enter' && !loading && handleSubmit()}
                        placeholder="Viết bình luận..."
                    />
                    <button onClick={handleSubmit} disabled={loading || !content.trim()}>
                        {loading ? "Đang gửi..." : "Gửi"}
                    </button>
                </div>
            </div>

        </Modal>
    );
}