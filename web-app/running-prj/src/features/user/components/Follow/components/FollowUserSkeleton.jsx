import React from "react";

const FollowUserSkeleton = () => (
    <div className="follow-card follow-card--skeleton">
        <div className="follow-card__avatar skeleton-block" />
        <div className="follow-card__content">
            <div className="skeleton-block skeleton-block--title" />
            <div className="skeleton-block skeleton-block--line" />
            <div className="skeleton-block skeleton-block--line short" />
        </div>
        <div className="follow-card__actions">
            <div className="skeleton-block skeleton-block--button" />
        </div>
    </div>
);

export default FollowUserSkeleton;
