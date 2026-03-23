import React from "react";
import { MapPin, MessageCircle, Phone } from "lucide-react";

const RELATION_META = {
    discover: { badge: "Gần bạn", badgeClassName: "follow-badge follow-badge--discover", buttonLabel: "Follow", buttonClassName: "follow-primary-btn" },
    following: { badge: "Following", badgeClassName: "follow-badge follow-badge--following", buttonLabel: "Following", buttonClassName: "follow-secondary-btn" },
    follower: { badge: "Follows you", badgeClassName: "follow-badge follow-badge--follower", buttonLabel: "Follow back", buttonClassName: "follow-primary-btn" },
    mutual: { badge: "Mutual", badgeClassName: "follow-badge follow-badge--mutual", buttonLabel: "Following", buttonClassName: "follow-secondary-btn" },
};

const FollowUserCard = ({ user, onToggleFollow, onMessage, isPending }) => {
    const meta = RELATION_META[user.relation] || RELATION_META.discover;
    const distanceLabel = user.distanceKm != null ? `${Number(user.distanceKm).toFixed(1)} km` : "Chưa có tọa độ";

    return (
        <article className={`follow-card ${user.isFollowing ? "follow-card--active" : ""}`}>
            <img src={user.avatarUrl} alt={user.fullName} className="follow-card__avatar" />
            <div className="follow-card__content">
                <div className="follow-card__topline">
                    <div>
                        <h3 className="follow-card__name">{user.fullName}</h3>
                        <p className="follow-card__handle">@{user.username}</p>
                    </div>
                    <span className={meta.badgeClassName}>{meta.badge}</span>
                </div>
                <p className="follow-card__headline">{user.headline}</p>
                <div className="follow-card__meta">
                    <span><Phone size={15} />{user.phoneNumber}</span>
                    <span><MapPin size={15} />{distanceLabel}</span>
                </div>
            </div>
            <div className="follow-card__actions">
                <button
                    type="button"
                    className={`${meta.buttonClassName} ${isPending ? "is-pending" : ""}`}
                    onClick={() => onToggleFollow(user)}
                    disabled={isPending}
                >
                    {isPending ? "Updating..." : meta.buttonLabel}
                </button>
                {(user.isFollowing || user.relation === "mutual") && (
                    <button type="button" className="follow-icon-btn" onClick={() => onMessage(user)}>
                        <MessageCircle size={18} />
                    </button>
                )}
            </div>
        </article>
    );
};

export default FollowUserCard;
