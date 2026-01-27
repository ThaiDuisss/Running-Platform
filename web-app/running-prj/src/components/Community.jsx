import React from 'react';
import { Container, Button } from 'react-bootstrap';
import { Users } from 'lucide-react';

const Community = () => {
    return (
        <section id="community" className="py-5 bg-white text-center">
            <Container className="py-5 border-top border-bottom">
                <div className="d-flex justify-content-center mb-4">
                    <div className="bg-light p-3 rounded-circle">
                        <Users size={40} className="text-dark" />
                    </div>
                </div>
                <h2 className="display-5 fw-bold mb-4">MẠNG LƯỚI KẾT NỐI KHÔNG GIỚI HẠN</h2>
                <p className="lead text-muted mx-auto mb-5" style={{ maxWidth: '800px' }}>
                    Tham gia cùng hơn 10.000 runner và trekker khác để chia sẻ lộ trình,
                    bí quyết luyện tập và cùng nhau chinh phục những đỉnh cao mới tại Việt Nam.
                </p>
                <Button variant="dark" className="rounded-0 px-5 py-3 fw-bold">
                    THAM GIA NGAY
                </Button>
            </Container>
        </section>
    );
};

export default Community;