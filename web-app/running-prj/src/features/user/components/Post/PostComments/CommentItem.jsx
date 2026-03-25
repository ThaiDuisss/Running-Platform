import React, { useState, useEffect, useRef, useContext } from "react";
import { replyComment, reactComment, countCommentReply } from "@/features/admin/users/services/UserService";
import "@/features/user/components/Post/Feed.css";
import { getCommentReaction } from "@/features/admin/users/services/UserService";
import ReactionModal from "../PostReactions/ReactionModal";
import { AuthDataContext } from "@/app/providers/AuthProvider";

const REACTIONS = ["👍", "❤️", "😂", "😮", "😢", "😡"];

const REACTION_MAP = {
    "👍": "LIKE",
    "❤️": "LOVE",
    "😂": "HAHA",
    "😮": "WOW",
    "😢": "SAD",
    "😡": "ANGRY"
};

export default function CommentItem({ comment, postId, refresh, level = 0 }) {
    if (!comment) return null;

    const { user } = useContext(AuthDataContext);
    const [showReply, setShowReply] = useState(false);
    const [replyContent, setReplyContent] = useState("");
    const [loading, setLoading] = useState(false);
    const [showReactions, setShowReactions] = useState(false);
    const [myReaction, setMyReaction] = useState(comment?.userReaction || null);
    const [totalReacts, setTotalReacts] = useState(comment?.totalReactions || 0);
    const hideTimeout = useRef(null);
    const [showReactionModal, setShowReactionModal] = useState(false);



    useEffect(() => {
        const fetchCount = async () => {
            try {
                const res = await countCommentReply(comment.id);
                setTotalReacts(res.data.data || 0);
            } catch (err) {
                console.error("Lỗi load số reaction:", err);
            }
        };

        if (comment?.id) {
            fetchCount();
        }
    }, [comment.id]);

    const handleReply = async () => {
        if (!replyContent.trim()) return;
        try {
            setLoading(true);
            await replyComment({
                postId,
                content: replyContent,
                parentCommentId: comment.id
            });
            setReplyContent("");
            setShowReply(false);
            refresh();
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleMouseEnter = () => {
        clearTimeout(hideTimeout.current);
        setShowReactions(true);
    };

    const handleMouseLeave = () => {
        hideTimeout.current = setTimeout(() => {
            setShowReactions(false);
        }, 300);
    };

    const handleSelectReaction = async (emoji) => {
        const type = REACTION_MAP[emoji] || 'LIKE';
        const isRemoving = myReaction === emoji;
        const oldReaction = myReaction;
        const oldTotal = totalReacts;

        setMyReaction(isRemoving ? null : emoji);
        setTotalReacts(prev => isRemoving ? Math.max(0, prev - 1) : (oldReaction ? prev : prev + 1));
        setShowReactions(false);

        try {
            await reactComment(comment.id, type);
            const res = await countCommentReply(comment.id);
            setTotalReacts(res.data.data || 0);

        } catch (err) {
            console.error("Lỗi react comment:", err);
            setMyReaction(oldReaction);
            setTotalReacts(oldTotal);
            alert("Không thể thực hiện hành động này. Vui lòng thử lại!");
        }
    };
    const indentMargin = level === 0 ? 0 : 40;

    return (
        <div className="comment-wrapper" style={{ marginLeft: indentMargin }}>
            <div className="comment-main">
                <img
                    src={user?.avatar}
                    alt="avatar"
                    className="comment-avatar"
                />

                <div className="comment-content-area">
                    <div className="comment-bubble-wrapper">
                        <div className="comment-bubble">
                            <span className="comment-author">{comment?.username || "Người dùng ẩn danh"}</span>
                            <span className="comment-text">{comment?.content || ""}</span>
                        </div>

                        {totalReacts > 0 && (
                            <div
                                className="reaction-badge"
                                style={{ cursor: "pointer" }}
                                onClick={() => setShowReactionModal(true)}
                            >
                                <span>{myReaction || "👍"}</span>
                                <span style={{ marginLeft: 4 }}>{totalReacts} người</span>
                            </div>
                        )}
                    </div>

                    <div className="comment-actions">
                        <div
                            className="reaction-container"
                            onMouseEnter={handleMouseEnter}
                            onMouseLeave={handleMouseLeave}
                        >
                            <span
                                onClick={() => handleSelectReaction('👍')}
                                style={{
                                    color: myReaction === '❤️' ? '#f02849' : myReaction ? '#2078f4' : 'inherit',
                                    fontWeight: myReaction ? 700 : 600,
                                    cursor: 'pointer'
                                }}
                            >
                                {myReaction === '👍' ? 'Thích' : myReaction ? myReaction : 'Thích'}
                            </span>

                            {showReactions && (
                                <div className="reaction-picker">
                                    {REACTIONS.map(emoji => (
                                        <span
                                            key={emoji}
                                            className="reaction-icon"
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                handleSelectReaction(emoji);
                                            }}
                                        >
                                            {emoji}
                                        </span>
                                    ))}
                                </div>
                            )}
                        </div>

                        <span onClick={() => setShowReply(!showReply)} style={{ cursor: 'pointer' }}>Phản hồi</span>
                    </div>
                </div>
            </div>

            {showReply && (
                <div className="reply-input-wrapper">
                    <img
                        src={`https://i.pravatar.cc/32?u=guest`}
                        alt="avatar"
                        className="comment-avatar"
                        style={{ width: 24, height: 24 }}
                    />
                    <div className="comment-input-box" style={{ borderRadius: 16 }}>
                        <input
                            value={replyContent}
                            onChange={(e) => setReplyContent(e.target.value)}
                            placeholder={`Trả lời ${comment?.username || 'người này'}...`}
                            autoFocus
                        />
                        <button onClick={handleReply} disabled={loading}>
                            {loading ? "..." : "Gửi"}
                        </button>
                    </div>
                </div>
            )}

            {Array.isArray(comment?.replies) && comment.replies.length > 0 && (
                <div className="replies-container">
                    {comment.replies.map((reply, index) => (
                        <CommentItem
                            key={reply?.id || index}
                            comment={reply}
                            postId={postId}
                            refresh={refresh}
                            level={level + 1}
                        />
                    ))}
                </div>
            )}

            <ReactionModal
                show={showReactionModal}
                onClose={() => setShowReactionModal(false)}
                postId={comment.id}
                fetchApi={getCommentReaction}
            />
        </div>

    );
}