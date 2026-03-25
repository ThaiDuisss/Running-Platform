import React, { useEffect, useState } from "react";
import { Modal, ListGroup, Spinner } from "react-bootstrap";
import { getPostReactions } from "@/features/admin/users/services/UserService";

const REACTION_TYPES = [
    { type: "LIKE", icon: "👍" },
    { type: "LOVE", icon: "❤️" },
    { type: "HAHA", icon: "😂" },
    { type: "WOW", icon: "😮" },
    { type: "SAD", icon: "😢" },
    { type: "ANGRY", icon: "😡" }
];

export default function ReactionModal({ show, onClose, postId, fetchApi }) {
    const [list, setList] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (show && postId) {
            loadReactions();
        }
    }, [show, postId, fetchApi]);

    const loadReactions = async () => {
        setLoading(true);
        try {
            let res;

            if (fetchApi) {
                res = await fetchApi(postId);
            } else {
                res = await getPostReactions(postId);
            }

            setList(res.data.data.content);
        } catch (error) {
            console.error("Lỗi khi load reactions:", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Modal show={show} onHide={onClose} centered size="sm">
            <Modal.Header closeButton>
                <Modal.Title style={{ fontSize: 16, fontWeight: 600 }}>
                    Người đã bày tỏ cảm xúc
                </Modal.Title>
            </Modal.Header>

            <Modal.Body style={{ maxHeight: 400, overflowY: "auto", padding: 0 }}>
                {loading ? (
                    <div className="text-center p-3">
                        <Spinner animation="border" size="sm" />
                    </div>
                ) : (
                    <ListGroup variant="flush">
                        {list.map((item, index) => {
                            const reaction = REACTION_TYPES.find(
                                r => r.type === (item.reactionType || item.reaction)
                            );

                            return (
                                <ListGroup.Item
                                    key={index}
                                    className="d-flex align-items-center border-0"
                                    style={{ padding: "8px 12px" }}
                                >
                                    <img
                                        src={`https://i.pravatar.cc/32?u=${item.username}`}
                                        alt=""
                                        className="rounded-circle me-2"
                                    />

                                    <div className="flex-grow-1">
                                        <strong style={{ fontSize: 14 }}>
                                            {item.username}
                                        </strong>
                                    </div>

                                    {/* vẫn giữ icon nhỏ bên phải (optional) */}
                                    <span>{reaction?.icon}</span>
                                </ListGroup.Item>
                            );
                        })}
                    </ListGroup>
                )}
            </Modal.Body>
        </Modal>
    );
}