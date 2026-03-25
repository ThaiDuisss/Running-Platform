import React, { useContext, useState } from "react";
import { Modal } from "react-bootstrap";
import { userCreatePosts } from "@/features/admin/users/services/UserService";
import "@/features/user/components/Post/Feed.css";
import { AuthDataContext } from "@/app/providers/AuthProvider";


export default function CreatePost({ onSuccess }) {
    const [show, setShow] = useState(false);
    const [content, setContent] = useState("");
    const [images, setImages] = useState([]);
    const [loading, setLoading] = useState(false);
    const { user } = useContext(AuthDataContext);

    const handleFileChange = (e) => {
        const files = Array.from(e.target.files);

        files.forEach(file => {
            const reader = new FileReader();

            reader.onloadend = () => {
                const base64 = reader.result.split(",")[1];
                setImages(prev => [...prev, base64]);
            };

            reader.readAsDataURL(file);
        });
    };

    const handleClose = () => {
        if (loading) return;
        setShow(false);
        setContent("");
        setImages([]);
    };

    const handleSubmit = async () => {
        if (!content && images.length === 0) return;

        try {
            setLoading(true);

            await userCreatePosts({ content, images });
            handleClose();

            onSuccess && onSuccess();

        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <div className="modern-card">
                <div className="create-post-top">
                    <img src={user?.imageUrl} alt="avatar" />
                    <input
                        placeholder="Bạn đang nghĩ gì thế?"
                        readOnly
                        onClick={() => setShow(true)}
                    />
                </div>

                {/* <div className="create-post-actions">
                    <div className="create-post-btn" onClick={() => setShow(true)}>
                        🔴 Video trực tiếp
                    </div>
                    <div className="create-post-btn" onClick={() => setShow(true)}>
                        🖼️ Ảnh/Video
                    </div>
                    <div className="create-post-btn" onClick={() => setShow(true)}>
                        😀 Cảm xúc
                    </div>
                </div> */}
            </div>

            {/* Modal */}
            <Modal show={show} onHide={handleClose} centered>
                <div className="modal-content">

                    {/* Header */}
                    <div className="modal-header">
                        <h5 className="modal-title">Tạo bài viết</h5>
                        <button className="btn-close" onClick={handleClose} />
                    </div>

                    {/* Body */}
                    <div className="modal-body">

                        <textarea
                            className="create-post-textarea"
                            placeholder="Bạn đang nghĩ gì thế?"
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                        />

                        <input
                            type="file"
                            multiple
                            onChange={handleFileChange}
                            disabled={loading}
                        />

                        {/* Preview */}
                        {images.length > 0 && (
                            <div className="preview-list">
                                {images.map((img, index) => (
                                    <img
                                        key={index}
                                        src={`data:image/png;base64,${img}`}
                                        alt="preview"
                                    />
                                ))}
                            </div>
                        )}

                    </div>

                    {/* Footer */}
                    <div className="modal-footer">
                        <button className="btn-secondary" onClick={handleClose}>
                            Huỷ
                        </button>

                        <button
                            className="btn-primary"
                            onClick={handleSubmit}
                            disabled={loading}
                        >
                            {loading ? "Đang đăng..." : "Đăng bài"}
                        </button>
                    </div>

                </div>
            </Modal>
            <style>
                {`
                .create-post-trigger {
                    display: flex;
                    align-items: center;
                    gap: 10px;
                    background: #fff;
                    border: 1.5px solid #c2dff7;
                    border-radius: 12px;
                    padding: 10px;
                    cursor: pointer;
                    margin-bottom: 16px;
                }

                .create-post-trigger img {
                    width: 40px;
                    height: 40px;
                    border-radius: 50%;
                }

                .create-post-trigger input {
                    flex: 1;
                    border: none;
                    background: #f7fbff;
                    padding: 10px;
                    border-radius: 10px;
                    cursor: pointer;
                }

                .create-post-trigger:hover {
                    background: #f7fbff;
                }

                /* Textarea */
                .create-post-textarea {
                    width: 100%;
                    min-height: 100px;
                    border: 1.5px solid #c2dff7;
                    border-radius: 10px;
                    padding: 10px;
                    margin-bottom: 10px;
                    resize: none;
                }

                /* Preview */
                .preview-list {
                    display: flex;
                    gap: 10px;
                    margin-top: 10px;
                    flex-wrap: wrap;
                }

                .preview-list img {
                    width: 80px;
                    height: 80px;
                    object-fit: cover;
                    border-radius: 8px;
                }

                /* Modal fix đẹp */
                .modal-content {
                    border-radius: 12px;
                }
                `}
            </style>
        </>
    );
}

