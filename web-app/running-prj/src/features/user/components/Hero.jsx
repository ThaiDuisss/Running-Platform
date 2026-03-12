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
                    <div>
                        <a href="https://play.google.com/store/apps/details?id=com.yourapp">
                            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/78/Google_Play_Store_badge_EN.svg/1280px-Google_Play_Store_badge_EN.svg.png" height="60" />
                        </a>
                        <a href="https://play.google.com/store/apps/details?id=com.yourapp">
                            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/3c/Download_on_the_App_Store_Badge.svg/960px-Download_on_the_App_Store_Badge.svg.png" height="60" />
                        </a>
                    </div>
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