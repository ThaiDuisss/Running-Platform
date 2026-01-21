import React from 'react';
import { Container, Button } from 'react-bootstrap';
import { ArrowRight } from 'lucide-react';

const Hero = () => {
    return (
        <section className="hero-section text-white d-flex align-items-center">
            <Container>
                <div className="max-w-700">
                    <h1 className="display-1 fw-bold mb-3 tracking-widest text-uppercase">
                        Chinh phục<br />chân trời
                    </h1>
                    <p className="fs-4 mb-5 opacity-75">
                        Unlock your potential. Explore, train, conquer.
                    </p>
                    <Button variant="light" className="rounded-0 px-5 py-3 fw-bold d-inline-flex align-items-center gap-2">
                        BẮT ĐẦU HÀNH TRÌNH <ArrowRight size={20} />
                    </Button>
                </div>
            </Container>
        </section>
    );
};

export default Hero;