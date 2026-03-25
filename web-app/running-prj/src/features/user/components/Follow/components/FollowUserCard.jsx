import React from "react";
import { MapPin, MessageCircle, Phone } from "lucide-react";
import { Badge, Button, Card } from "react-bootstrap";

const RELATION_META = {
    discover: { badge: "Gần bạn", badgeClassName: "follow-badge follow-badge--discover", buttonLabel: "Follow", buttonVariant: "dark", buttonClassName: "follow-action-btn--primary" },
    following: { badge: "Following", badgeClassName: "follow-badge follow-badge--following", buttonLabel: "Unfollow", buttonVariant: "outline-dark", buttonClassName: "follow-action-btn--dark" },
    followers: { badge: "Follows you", badgeClassName: "follow-badge follow-badge--followers", buttonLabel: "Follow back", buttonVariant: "outline-dark", buttonClassName: "follow-action-btn--primary" },
    friend: { badge: "Friend", badgeClassName: "follow-badge follow-badge--friend", buttonLabel: "Unfollow", buttonVariant: "dark", buttonClassName: "follow-action-btn--dark" },
};

const FollowUserCard = ({ user, onToggleFollow, onMessage, isPending }) => {
    const meta = RELATION_META[user.relation] || RELATION_META.discover;
    const distanceLabel = user.distanceKm != null ? `${Number(user.distanceKm).toFixed(1)} km` : "Chưa có tọa độ";

    return (
        <Card className={`follow-card border-0 ${user.isFollowing ? "follow-card--active" : ""}`}>
            <img src={user.avatarUrl || "/default-avatar.png"} alt={user.fullName} className="follow-card__avatar" />
            <div className="follow-card__content">
                <div className="follow-card__topline">
                    <div>
                        <h3 className="follow-card__name">{user.fullName}</h3>
                        <p className="follow-card__handle">@{user.username}</p>
                    </div>
                    <Badge pill className={meta.badgeClassName}>{meta.badge}</Badge>
                </div>
                <p className="follow-card__headline">{user.headline || user.location || "Runner near you"}</p>
                <div className="follow-card__meta">
                    <span><Phone size={15} />{user.phoneNumber}</span>
                    <span><MapPin size={15} />{distanceLabel}</span>
                </div>
            </div>
            <div className="follow-card__actions">
                <Button
                    type="button"
                    variant={meta.buttonVariant}
                    className={`follow-action-btn ${meta.buttonClassName} ${isPending ? "is-pending" : ""}`}
                    onClick={() => onToggleFollow(user)}
                    disabled={isPending}
                >
                    {isPending ? "Updating..." : meta.buttonLabel}
                </Button>
                {(user.isFollowing || user.relation === "friend") && (
                    <Button type="button" variant="light" className="follow-icon-btn" onClick={() => onMessage(user)}>
                        <MessageCircle size={18} />
                    </Button>
                )}
            </div>
        </Card>
    );
};

export default FollowUserCard;
