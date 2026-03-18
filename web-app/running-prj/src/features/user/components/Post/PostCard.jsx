import React from "react";
import { Card } from "react-bootstrap";

export default function PostCard({ post }) {

    return (
        <Card className="post" style={{ borderRadius: 12, marginBottom: 15 }}>

            {/* HEADER */}
            <Card.Body className="post-header" style={{ display: "flex", gap: 10 }}>

                <img
                    src={`https://i.pravatar.cc/40?u=${post.username}`}
                    alt="avatar"
                    style={{ borderRadius: "50%" }}
                />

                <div>
                    <div style={{ fontWeight: 600 }}>
                        {post.username}
                    </div>

                    <div style={{ fontSize: 12, color: "#65676b" }}>
                        PUBLIC · 🌍
                    </div>
                </div>

            </Card.Body>

            {/* CONTENT */}
            <Card.Body className="post-content">

                {/* TEXT */}
                {post.content && (
                    <div style={{ marginBottom: 10, fontSize: 15 }}>
                        {post.content}
                    </div>
                )}

                {/* IMAGES */}
                {post.images && post.images.length > 0 && (
                    <div
                        style={{
                            display: "grid",
                            gridTemplateColumns: post.images.length === 1
                                ? "1fr"
                                : "1fr 1fr",
                            gap: 6
                        }}
                    >
                        {post.images.map((img, index) => (
                            <img
                                key={index}
                                src={img}
                                alt="post"
                                style={{
                                    width: "100%",
                                    borderRadius: 8,
                                    objectFit: "cover",
                                    maxHeight: 400
                                }}
                            />
                        ))}
                    </div>
                )}

            </Card.Body>

            {/* ACTION BAR */}
            <Card.Body
                className="actions"
                style={{
                    display: "flex",
                    justifyContent: "space-around",
                    borderTop: "1px solid #eee"
                }}
            >

                <div className="action-btn">👍 Like</div>
                <div className="action-btn">💬 Comment</div>
                <div className="action-btn">↗ Share</div>

            </Card.Body>

        </Card>
    );
}