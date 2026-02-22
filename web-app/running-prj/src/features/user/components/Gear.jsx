import React from 'react';
import { Container, Row, Col, Button } from 'react-bootstrap';

const Gear = () => (
    <section id="shop" className="py-5 overflow-hidden">
        <Container>
            <Row className="align-items-center">
                <Col md={5} className="mb-5 mb-md-0">
                    <h2 className="display-4 fw-bold mb-4">TRANG BỊ CHUYÊN DỤNG</h2>
                    <p className="text-muted lead mb-5">Nâng cấp trải nghiệm chạy bộ của bạn với những dòng sản phẩm bền bỉ nhất.</p>
                    <Button variant="outline-dark" className="rounded-0 px-4 py-2 fw-bold">GHÉ THĂM CỬA HÀNG</Button>
                </Col>
                <Col md={7}>
                    <div className="gear-bg-accent p-5">
                        <img
                            src="https://images.unsplash.com/photo-1542291026-7eec264c27ff"
                            alt="Running shoes"
                            className="img-fluid gear-float-img"
                        />
                    </div>
                </Col>
            </Row>
        </Container>
    </section>
);

export default Gear;