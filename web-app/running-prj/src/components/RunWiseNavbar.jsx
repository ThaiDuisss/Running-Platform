import React, { useState, useEffect } from 'react';
import { Navbar, Nav, Container, Button } from 'react-bootstrap';

const RunWiseNavbar = () => {
    const [scrolled, setScrolled] = useState(false);

    useEffect(() => {
        const handleScroll = () => setScrolled(window.scrollY > 50);
        window.addEventListener('scroll', handleScroll);
        return () => window.removeEventListener('scroll', handleScroll);
    }, []);

    return (
        <Navbar bg="white" expand="lg" sticky="top" className={`py-3 transition-all ${scrolled ? 'shadow-sm' : ''}`}>
            <Container>
                <Navbar.Brand href="#" className="fw-bold fs-3 italic tracking-tighter">
                    RunWise
                </Navbar.Brand>
                <Navbar.Toggle aria-controls="main-nav" />
                <Navbar.Collapse id="main-nav">
                    <Nav className="mx-auto gap-3">
                        <Nav.Link href="#routes" className="fw-medium">Tuyến đường</Nav.Link>
                        <Nav.Link href="#community" className="fw-medium">Cộng đồng</Nav.Link>
                        <Nav.Link href="#shop" className="fw-medium">Cửa hàng</Nav.Link>
                        <Nav.Link href="#contact" className="fw-medium">Liên lạc chuyên gia</Nav.Link>
                    </Nav>
                    <Button variant="outline-dark" className="rounded-0 px-4 fw-bold">ĐĂNG KÝ</Button>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    );
};

export default RunWiseNavbar;