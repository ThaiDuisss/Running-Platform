import React from 'react';
import { Container, Row, Col } from 'react-bootstrap';
import { Facebook, Instagram, Youtube, Mail, Send } from 'lucide-react';

const Footer = () => {
    return (
        <footer className="bg-dark text-white pt-5 pb-3">
            <Container className="py-4">
                <Row className="gy-5">
                    <Col lg={4} md={12}>
                        <h2 className="fw-bold italic mb-3 tracking-tighter">RunWise</h2>
                        <p className="opacity-50 pe-lg-5">
                            Hành trình vạn dặm bắt đầu từ một bước chân. Chúng tôi đồng hành cùng bạn trên mọi cung đường trekking và chạy bộ tại Việt Nam.
                        </p>
                        <div className="d-flex gap-3 mt-4">
                            <a href="#" className="text-white opacity-75 hover-opacity-100"><Facebook size={20} /></a>
                            <a href="#" className="text-white opacity-75 hover-opacity-100"><Instagram size={20} /></a>
                            <a href="#" className="text-white opacity-75 hover-opacity-100"><Youtube size={20} /></a>
                        </div>
                    </Col>

                    <Col lg={2} md={4} className="col-6">
                        <h6 className="fw-bold mb-4 text-uppercase">Khám phá</h6>
                        <ul className="list-unstyled opacity-50 small">
                            <li className="mb-2"><a href="#" className="text-white text-decoration-none">Tuyến đường</a></li>
                            <li className="mb-2"><a href="#" className="text-white text-decoration-none">Cộng đồng</a></li>
                            <li className="mb-2"><a href="#" className="text-white text-decoration-none">Sự kiện</a></li>
                        </ul>
                    </Col>

                    <Col lg={2} md={4} className="col-6">
                        <h6 className="fw-bold mb-4 text-uppercase">Hỗ trợ</h6>
                        <ul className="list-unstyled opacity-50 small">
                            <li className="mb-2"><a href="#" className="text-white text-decoration-none">Chuyên gia</a></li>
                            <li className="mb-2"><a href="#" className="text-white text-decoration-none">Chính sách bảo mật</a></li>
                            <li className="mb-2"><a href="#" className="text-white text-decoration-none">Điều khoản</a></li>
                        </ul>
                    </Col>

                    <Col lg={4} md={4}>
                        <h6 className="fw-bold mb-4 text-uppercase">Bản tin</h6>
                        <p className="small opacity-50 mb-4">Nhận cập nhật về các cung đường mới nhất.</p>
                        <div className="d-flex border-bottom border-secondary pb-2">
                            <input
                                type="email"
                                placeholder="Email của bạn..."
                                className="bg-transparent border-0 text-white w-100 small"
                                style={{ outline: 'none' }}
                            />
                            <Send size={18} className="opacity-50" />
                        </div>
                    </Col>
                </Row>

                <hr className="my-5 opacity-10" />

                <div className="d-flex flex-column flex-md-row justify-content-between align-items-center opacity-50 small">
                    <p>© 2024 VELOUITY VIETNAM. All rights reserved.</p>
                    <p>Thiết kế bởi Senior Frontend Team</p>
                </div>
            </Container>
        </footer>
    );
};

export default Footer;