import React, { useContext, useEffect, useState, useRef } from "react";
import { Card } from "react-bootstrap";
import CommentModal from "./PostComments/CommentModal";
import ReactionModal from "./PostReactions/ReactionModal";
import { AuthDataContext } from "@/app/providers/AuthProvider";
import {
    countCommentReply,
    countReplyPost,
    reactPost
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

export default function PostCard({ post }) {
    const { user } = useContext(AuthDataContext);
    const [showComment, setShowComment] = useState(false);
    
    const [isExpanded, setIsExpanded] = useState(false);
    const MAX_LENGTH = 150; 
    
    const [showOptions, setShowOptions] = useState(false);
    const [countReply, setCountReply] = useState(0);
    const [showReactionModal, setShowReactionModal] = useState(false);
    const hideTimeout = useRef(null);

    const userKey = user?.id || user?.userId || user?.username || "guest";
    const localStorageKey = `post-reaction-${post?.id}-${userKey}`;
    const [countComment, setCountComment] = useState(0);

    const getStoredReaction = () => {
        try {
            return localStorage.getItem(localStorageKey);
        } catch (e) {
            return null;
        }
    };

    const [reaction, setReaction] = useState(
        post?.reaction || post?.reactionType || post?.userReaction || getStoredReaction() || null
    );

    useEffect(() => {
        fetchCountReply();
        fetchCountCommentReacts();  
    }, [post?.id, userKey]);

    useEffect(() => {
        const serverReaction = post?.reaction || post?.reactionType || post?.userReaction || getStoredReaction() || null;
        setReaction(serverReaction);
        try {
            if (serverReaction) localStorage.setItem(localStorageKey, serverReaction);
            else localStorage.removeItem(localStorageKey);
        } catch (e) {}
    }, [post, userKey]);

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
            setCountComment(res.data.data);
        } catch (error) {
            console.error("Failed to fetch comment reaction count:", error);
        }
    };


    const reactPosts = async (postId, reactionType) => {
        try {
            const res = await reactPost(postId, reactionType);
            const serverReaction = res.data.data?.reaction || res.data.data?.reactionType || null;
            const wasReactionBefore = Boolean(reaction);

            if (serverReaction) {
                setReaction(serverReaction);
                try { localStorage.setItem(localStorageKey, serverReaction); } catch (e) {}
            } else {
                setReaction(null);
                try { localStorage.removeItem(localStorageKey); } catch (e) {}
            }

            if (!wasReactionBefore && serverReaction) setCountReply(prev => prev + 1);
            else if (wasReactionBefore && !serverReaction) setCountReply(prev => Math.max(prev - 1, 0));

            await fetchCountReply();
            await fetchCountCommentReacts();
            setShowOptions(false);

        } catch (error) {
            console.error("Failed to react:", error);
        }
    };

    const currentReactionConfig = REACTION_TYPES.find(r => r.type === reaction);

    const handleMouseEnter = () => {
        clearTimeout(hideTimeout.current);
        setShowOptions(true);
    };

    const handleMouseLeave = () => {
        hideTimeout.current = setTimeout(() => setShowOptions(false), 300);
    };

    const contentText = post?.content || "";
    const shouldTruncate = contentText.length > MAX_LENGTH;

    return (
        <>
            <div className="modern-card" style={{ marginBottom: "16px" }}>
                
                {/* HEADER */}
                <div className="post-header">
                    <img src={post.avatar} alt="avatar" />
                    <div>
                        <div className="post-author">{post.username}</div>
                        {/* <div className="post-time">Vừa xong · 🌍</div> */}  
                    </div>
                </div>

                {/* CONTENT */}
                <div className="post-content">
                    {post.content && (
                        <div className="post-content-text">
                            {isExpanded || !shouldTruncate 
                                ? contentText 
                                : `${contentText.slice(0, MAX_LENGTH)}...`}
                            
                            {shouldTruncate && (
                                <span 
                                    onClick={() => setIsExpanded(!isExpanded)}
                                    style={{ 
                                        fontWeight: "600", 
                                        cursor: "pointer", 
                                        color: "var(--text-secondary, #65676B)",
                                        marginLeft: "5px" 
                                    }}
                                >
                                    {isExpanded ? "Ẩn bớt" : "Xem thêm"}
                                </span>
                            )}
                        </div>
                    )}

                    {post.images && post.images.length > 0 && (
                        <div 
                            className="post-images" 
                            style={{ 
                                gridTemplateColumns: post.images.length === 1 ? "1fr" : "1fr 1fr",
                                marginTop: "12px"
                            }}
                        >
                            {post.images.map((img, index) => (
                                <img key={index} src={img} alt="post media" style={{ width: "100%", borderRadius: "8px" }} />
                            ))}
                        </div>
                    )}
                </div>

                <div className="post-stats">
                    <div 
                        onClick={() => setShowReactionModal(true)} 
                        style={{ cursor: 'pointer', hover: { textDecoration: 'underline' } }}
                        className="reaction-count-text"
                    >
                        <span style={{ fontSize: '18px', marginRight: '4px' }}>
                            {currentReactionConfig ? currentReactionConfig.icon : "👍"}
                        </span>
                        {countReply} người đã bày tỏ cảm xúc
                    </div>
                    <div>{countComment} Bình luận</div> 
                </div>

                {/* ACTION BAR */}
                <div className="post-actions">
                    <div
                        className="action-btn"
                        onMouseEnter={handleMouseEnter}
                        onMouseLeave={handleMouseLeave}
                        style={{ position: 'relative' }} 
                    >
                        <div 
                            style={{ 
                                display: 'flex', 
                                alignItems: 'center', 
                                gap: '6px',
                                color: currentReactionConfig ? currentReactionConfig.color : 'inherit',
                                fontWeight: currentReactionConfig ? 'bold' : 'normal'
                            }}
                            onClick={() => reactPosts(post.id, "LIKE")}
                        >
                            <span style={{ fontSize: '20px' }}>
                                {currentReactionConfig ? currentReactionConfig.icon : "👍"}
                            </span>
                            {currentReactionConfig ? currentReactionConfig.label : "Thích"}
                        </div>

                        {showOptions && (
                            <div className="reaction-picker" style={{ bottom: '100%', left: '0', marginBottom: '10px' }}>
                                {REACTION_TYPES.map(r => (
                                    <span 
                                        key={r.type} 
                                        className="reaction-icon"
                                        title={r.label}
                                        onClick={(e) => {
                                            e.stopPropagation(); 
                                            reactPosts(post.id, r.type);
                                        }}
                                    >
                                        {r.icon}
                                    </span>
                                ))}
                            </div>
                        )}
                    </div>

                    <div className="action-btn" onClick={() => setShowComment(true)}>
                        💬 Bình luận
                    </div>

                    {/* <div className="action-btn">
                        ↗ Chia sẻ
                    </div> */}
                </div>
            </div>

            <ReactionModal 
                show={showReactionModal} 
                onClose={() => setShowReactionModal(false)} 
                postId={post.id} 
            />

            <CommentModal
                show={showComment}
                onClose={() => setShowComment(false)}
                post={post}
            />
        </>
    );
}