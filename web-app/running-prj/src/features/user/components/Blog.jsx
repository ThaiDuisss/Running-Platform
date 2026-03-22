import React, { useEffect, useState } from 'react';
import { Container, Row, Col, Card, Alert, Spinner } from 'react-bootstrap';
import { ChevronRight } from 'lucide-react';
import { NavLink } from 'react-router-dom';
import { getArticleWithPaginateAPI } from '@/features/admin/article/services/ArticleService';

const formatPublishedAt = (publishedAt) => {
    if (!publishedAt) return "";

    if (Array.isArray(publishedAt)) {
        const [year, month, day, hour = 0, minute = 0, second = 0] = publishedAt;
        return new Date(year, month - 1, day, hour, minute, second).toLocaleDateString("vi-VN");
    }

    return new Date(publishedAt).toLocaleDateString("vi-VN");
};

const Blog = () => {
    const [articles, setArticles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchArticles = async () => {
            try {
                setLoading(true);
                setError("");
                const response = await getArticleWithPaginateAPI(0, 9);
                setArticles(response?.content || []);
            } catch (err) {
                setError(err.message || "Không thể tải danh sách bài viết");
            } finally {
                setLoading(false);
            }
        };

        fetchArticles();
    }, []);

    return (
        <section className="py-5 bg-light">
            <Container className="py-5">
                <div className="text-center mb-5">
                    <h2 className="fw-bold italic display-6">BLOG & KIẾN THỨC</h2>
                    <div className="mx-auto bg-dark" style={{ width: '60px', height: '4px' }}></div>
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

                {!loading && !error && (
                    <Row>
                        {articles.length > 0 ? (
                            articles.map((article) => (
                                <Col lg={4} md={6} key={article.id} className="mb-4">
                                    <Card className="h-100 border-0 shadow-sm overflow-hidden">
                                        {article.thumbnailUrl && (
                                            <Card.Img
                                                variant="top"
                                                src={article.thumbnailUrl}
                                                alt={article.title}
                                                style={{ height: "220px", objectFit: "cover" }}
                                            />
                                        )}

                                        <Card.Body className="p-4">
                                            <span className="text-danger fw-bold small text-uppercase">
                                                {article.category}
                                            </span>
                                            <h5 className="fw-bold my-3">{article.title}</h5>
                                            <p className="text-muted small">
                                                {article.summary || "Chưa có mô tả ngắn cho bài viết này."}
                                            </p>
                                            <div className="d-flex justify-content-between align-items-center mt-4">
                                                <span className="text-muted extra-small">
                                                    {formatPublishedAt(article.publishedAt)}
                                                </span>
                                                <NavLink to={`/blog/${article.id}`} className="text-dark text-decoration-none fw-bold small">
                                                    Đọc thêm <ChevronRight size={14} />
                                                </NavLink>
                                            </div>
                                        </Card.Body>
                                    </Card>
                                </Col>
                            ))
                        ) : (
                            <Col xs={12}>
                                <Alert variant="light" className="text-center border">
                                    Chưa có bài viết nào để hiển thị.
                                </Alert>
                            </Col>
                        )}
                    </Row>
                )}
            </Container>
        </section>
    );
};

export default Blog;
