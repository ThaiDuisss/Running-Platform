import React from 'react';
import { Container, Row, Col, Card } from 'react-bootstrap';
import { ChevronRight } from 'lucide-react';

const blogPosts = [
    {
        id: 1,
        category: "Kỹ thuật",
        title: "Cách kiểm soát nhịp thở khi chạy dốc",
        desc: "Bí quyết giúp bạn duy trì sức bền trên các cung đường Tà Năng...",
        date: "15/05/2024"
    },
    {
        id: 2,
        category: "Trang bị",
        title: "Chọn giày Trail hay giày Road?",
        desc: "Sự khác biệt cốt lõi và cách chọn đôi giày phù hợp với địa hình...",
        date: "12/05/2024"
    },
    {
        id: 3,
        category: "Dinh dưỡng",
        title: "Nạp năng lượng đường dài",
        desc: "Chế độ dinh dưỡng tối ưu cho các cự ly trên 21km...",
        date: "10/05/2024"
    }
];

const Blog = () => {
    return (
        <section className="py-5 bg-light">
            <Container className="py-5">
                <div className="text-center mb-5">
                    <h2 className="fw-bold italic display-6">BLOG & KIẾN THỨC</h2>
                    <div className="mx-auto bg-dark" style={{ width: '60px', height: '4px' }}></div>
                </div>
                <Row>
                    {blogPosts.map(post => (
                        <Col lg={4} key={post.id} className="mb-4">
                            <Card className="h-100 border-0 shadow-sm p-3 hover-up">
                                <Card.Body>
                                    <span className="text-danger fw-bold small text-uppercase">{post.category}</span>
                                    <h5 className="fw-bold my-3">{post.title}</h5>
                                    <p className="text-muted small">{post.desc}</p>
                                    <div className="d-flex justify-content-between align-items-center mt-4">
                                        <span className="text-muted extra-small">{post.date}</span>
                                        <a href="#" className="text-dark text-decoration-none fw-bold small">
                                            Đọc thêm <ChevronRight size={14} />
                                        </a>
                                    </div>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
            </Container>
        </section>
    );
};

export default Blog;