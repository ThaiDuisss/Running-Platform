import { useEffect, useMemo, useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import Form from "react-bootstrap/Form";
import { createArticleAPI, updateArticleAPI } from "../services/ArticleService";
import { toast } from "react-toastify";

const FormArticle = ({ mode = "create", show, selectedArticle, onClose, fetchArticles }) => {
    const isUpdate = mode === "update";

    const initial = useMemo(() => {
        const a = selectedArticle || {};
        return {
            title: a.title || "",
            summary: a.summary || "",
            content: a.content || "",
            category: a.category || "",
            thumbnailUrl: a.thumbnailUrl || a.thumbnail || "",
        };
    }, [selectedArticle]);

    const [title, setTitle] = useState(initial.title);
    const [summary, setSummary] = useState(initial.summary);
    const [content, setContent] = useState(initial.content);
    const [category, setCategory] = useState(initial.category);
    const [image, setImage] = useState(null);
    const [preview, setPreview] = useState(initial.thumbnailUrl);
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        setTitle(initial.title);
        setSummary(initial.summary);
        setContent(initial.content);
        setCategory(initial.category);
        setImage(null);
        setPreview(null);
    }, [initial, show]);

    const handleSubmit = async () => {
        try {
            setSubmitting(true);

            const payload = {
                title,
                summary,
                content,
                category,
                image,
                thumbnailUrl: initial.thumbnailUrl,
            };

            if (isUpdate) {
                if (!selectedArticle?.id) throw new Error("Missing article id");
                await updateArticleAPI(selectedArticle.id, payload);
                toast.success("Update article successfully");
            } else {
                await createArticleAPI(payload);
                toast.success("Create article successfully");
            }

            await fetchArticles?.();
            onClose?.();
        } catch (error) {
            console.error(error);
            toast.error(error?.message || "Submit failed");
        } finally {
            setSubmitting(false);
        }
    };

    const handleThumbnailChange = (e) => {
        const file = e.target.files?.[0];
        if (!file) return;

        setImage(file);
        const previewUrl = URL.createObjectURL(file);
        setPreview(previewUrl);
    };

    return (
        <Modal show={show} onHide={onClose}>

            <Modal.Header closeButton>
                <Modal.Title>{isUpdate ? "Update Article" : "Create Article"}</Modal.Title>
            </Modal.Header>

            <Modal.Body>

                <div style={{ textAlign: "center", marginBottom: "20px" }}>
                    <label htmlFor="thumbnailUpload" style={{ cursor: "pointer" }}>
                        <img
                            src={preview || "https://via.placeholder.com/150"}
                            alt="thumbnail"
                            style={{
                                width: "150px",
                                height: "100px",
                                objectFit: "cover",
                                border: "2px solid #ddd"
                            }}
                        />
                    </label>

                    <input
                        id="thumbnailUpload"
                        type="file"
                        accept="image/*"
                        style={{ display: "none" }}
                        onChange={handleThumbnailChange}
                    />
                </div>

                <Form>

                    <Form.Group className="mb-3">
                        <Form.Label>Title</Form.Label>
                        <Form.Control
                            type="text"
                            value={title}
                            onChange={(e) => setTitle(e.target.value)}
                            disabled={submitting}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Summary</Form.Label>
                        <Form.Control
                            type="text"
                            value={summary}
                            onChange={(e) => setSummary(e.target.value)}
                            disabled={submitting}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Category</Form.Label>
                        <Form.Select
                            value={category}
                            onChange={(e) => setCategory(e.target.value)}
                            disabled={submitting}
                        >
                            <option value="">-- Chọn category --</option>
                            <option value="TECHNIQUE">TECHNIQUE</option>
                            <option value="EQUIPMENT">EQUIPMENT</option>
                            <option value="NUTRITION">NUTRITION</option>
                        </Form.Select>
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Content</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={4}
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                            disabled={submitting}
                        />
                    </Form.Group>

                </Form>

            </Modal.Body>

            <Modal.Footer>

                <Button variant="secondary" onClick={onClose}>
                    Cancel
                </Button>

                <Button variant="primary" onClick={handleSubmit} disabled={submitting}>
                    {isUpdate ? "Update" : "Create"}
                </Button>

            </Modal.Footer>

        </Modal>
    );
};

export default FormArticle;