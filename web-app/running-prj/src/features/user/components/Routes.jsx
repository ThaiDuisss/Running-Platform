import React from 'react';
import { Container, Row, Col, Card, Badge } from 'react-bootstrap';
import { MapPin } from 'lucide-react';

const routeData = [
    { id: 1, name: "Công viên Yên Sở", location: "Hà Nội", dist: "5km - 15km", img: "https://sinhtour.vn/wp-content/uploads/2024/09/cong-vien-yen-so-1.jpg" },
    { id: 2, name: "Tà Năng - Phan Dũng", location: "Lâm Đồng", dist: "30km", img: "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b" }
];

const Routes = () => (
    <section id="routes" className="py-5 bg-light">
        <Container className="py-5">
            <h2 className="fw-bold mb-5 border-start border-4 border-dark ps-3">TUYẾN ĐƯỜNG NỔI BẬT</h2>
            <Row>
                {routeData.map(item => (
                    <Col md={6} key={item.id} className="mb-4">
                        <Card className="border-0 shadow-sm overflow-hidden hover-up">
                            <Card.Img src={item.img} className="object-cover h-300" />
                            <Card.Body className="p-4">
                                <div className="d-flex justify-content-between">
                                    <Badge bg="dark" className="px-3 mb-2">{item.dist}</Badge>
                                    <span className="text-muted"><MapPin size={16} /> {item.location}</span>
                                </div>
                                <h4 className="fw-bold">{item.name}</h4>
                            </Card.Body>
                        </Card>
                    </Col>
                ))}
            </Row>
        </Container>
    </section>
);

export default Routes;