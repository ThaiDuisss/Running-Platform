import React, { useEffect, useState } from "react";
import { Alert, Badge, Button, Card, Col, Container, Row, Spinner } from "react-bootstrap";
import { NavLink, useParams } from "react-router-dom";
import { getArticleByIdAPI } from "@/features/admin/article/services/ArticleService";

const formatPublishedAt = (publishedAt) => {
    if (!publishedAt) return "";

    if (Array.isArray(publishedAt)) {
        const [year, month, day, hour = 0, minute = 0, second = 0] = publishedAt;
        return new Date(year, month - 1, day, hour, minute, second).toLocaleString("vi-VN");
    }

    return new Date(publishedAt).toLocaleString("vi-VN");
};

const BlogDetail = () => {
    const { id } = useParams();
    const [article, setArticle] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchArticle = async () => {
            try {
                setLoading(true);
                setError("");
                const response = await getArticleByIdAPI(id);
                setArticle(response);
            } catch (err) {
                setError(err.message || "Không thể tải bài viết");
            } finally {
                setLoading(false);
            }
        };

        fetchArticle();
    }, [id]);

    return (
        <section className="py-5 bg-light">
            <Container className="py-4">
                <Row className="justify-content-center">
                    <Col lg={9}>
                        <div className="mb-4">
                            <Button as={NavLink} to="/blog" variant="outline-dark" size="sm">
                                Quay lại blog
                            </Button>
                        </div>

                        {loading && (
                            <div className="text-center py-5">
                                <Spinner animation="border" variant="dark" />
                            </div>
                        )}

                        {!loading && error && (
                            <Alert variant="danger" className="text-center">
                                {error}
                            </Alert>
                        )}

                        {!loading && !error && article && (
                            <Card className="border-0 shadow-sm overflow-hidden">
                                {article.thumbnailUrl && (
                                    <Card.Img
                                        variant="top"
                                        src={article.thumbnailUrl}
                                        alt={article.title}
                                        style={{ maxHeight: "460px", objectFit: "cover" }}
                                    />
                                )}

                                <Card.Body className="p-4 p-md-5">
                                    <div className="mb-3">
                                        <Badge bg="danger-subtle" text="danger" className="px-3 py-2">
                                            {article.category}
                                        </Badge>
                                    </div>

                                    <h1 className="fw-bold mb-3">{article.title}</h1>

                                    <div className="text-muted small mb-4">
                                        {formatPublishedAt(article.publishedAt)}
                                    </div>

                                    {article.summary && (
                                        <p className="fs-5 text-secondary mb-4">{article.summary}</p>
                                    )}

                                    <div
                                        className="text-dark lh-lg"
                                        style={{ whiteSpace: "pre-line" }}
                                    >
                                        {article.content || "Bài viết này chưa có nội dung chi tiết."}
                                    </div>
                                </Card.Body>
                            </Card>
                        )}
                    </Col>
                </Row>
            </Container>
        </section>
    );
};

export default BlogDetail;
