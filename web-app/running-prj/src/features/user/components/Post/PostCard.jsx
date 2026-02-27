import React from "react";

export default function PostCard({ post }) {

    return (
        <div className="post">

            <div className="post-header">

                <img src="https://i.pravatar.cc/40" />

                <div>
                    <div className="name">{post.user}</div>
                    <div className="time">{post.time}</div>
                </div>

            </div>

            <div className="post-content">
                {post.content}
            </div>

            <div className="running-stats">

                <div>
                    <div className="stat-value">{post.distance}</div>
                    <div className="stat-label">Distance</div>
                </div>

                <div>
                    <div className="stat-value">{post.pace}</div>
                    <div className="stat-label">Pace</div>
                </div>

                <div>
                    <div className="stat-value">{post.duration}</div>
                    <div className="stat-label">Time</div>
                </div>

            </div>

            <div className="post-actions">

                <div>👍 Like</div>
                <div>💬 Comment</div>
                <div>↗ Share</div>

            </div>

        </div>
    )
}