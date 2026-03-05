import React from "react";
import { Card } from "react-bootstrap";

export default function PostCard({ post }) {

    return (
        <Card className="post">

            {/* HEADER */}
            <Card.Body className="post-header">

                <img
                    src="https://i.pravatar.cc/40"
                    alt="avatar"
                    className="avatar"
                />

                <div className="user-info">

                    <div className="user-name">
                        {post.user}
                    </div>

                    <div className="post-time">
                        {post.time} · 🌍
                    </div>

                </div>

            </Card.Body>


            {/* CONTENT */}
            <Card.Body className="post-content">

                <div className="post-text">
                    {post.content}
                </div>


                {/* RUNNING STATS */}
                <div className="running-box">

                    <div className="stat">
                        <div className="stat-value">
                            {post.distance}
                        </div>
                        <div className="stat-label">
                            Distance
                        </div>
                    </div>

                    <div className="stat">
                        <div className="stat-value">
                            {post.pace}
                        </div>
                        <div className="stat-label">
                            Pace
                        </div>
                    </div>

                    <div className="stat">
                        <div className="stat-value">
                            {post.duration}
                        </div>
                        <div className="stat-label">
                            Time
                        </div>
                    </div>

                </div>

            </Card.Body>


            {/* ACTION BAR */}
            <Card.Body className="actions">

                <div className="action-btn">
                    👍 Like
                </div>

                <div className="action-btn">
                    💬 Comment
                </div>

                <div className="action-btn">
                    ↗ Share
                </div>

            </Card.Body>

        </Card>
    );
}