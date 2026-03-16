import React from 'react';
import { Container, Button } from 'react-bootstrap';
import { ArrowRight } from 'lucide-react';
import { FaFacebook, FaInstagram, FaTiktok, FaXTwitter } from "react-icons/fa6";

const Hero = () => {
    const socials = [
        {
            name: "Facebook",
            icon: <FaFacebook />,
            url: "https://facebook.com",
            bg: "#1877F2"
        },
        {
            name: "Instagram",
            icon: <FaInstagram />,
            url: "https://instagram.com",
            bg: "#E4405F"
        },
        {
            name: "TikTok",
            icon: <FaTiktok />,
            url: "https://tiktok.com",
            bg: "#000000"
        },
        {
            name: "X",
            icon: <FaXTwitter />,
            url: "https://x.com",
            bg: "#000000"
        }
    ];
    return (
        <section className="hero-section text-white d-flex align-items-center">
            <Container>
                <div className="hero-left max-w-700 ">
                    <h1 className="display-2 fw-bold mb-2 tracking-widest text-uppercase">
                        Chinh phục<br />chân trời
                    </h1>

                    <p className="fs-4 mb-3 opacity-75">
                        Unlock your potential. Explore, train, conquer.
                    </p>
                    <Button variant="light" className="rounded-0 px-5 py-3 fw-bold d-inline-flex align-items-center gap-2 mb-3">
                        BẮT ĐẦU HÀNH TRÌNH <ArrowRight size={20} />
                    </Button>
                </div>
                <div className='hero-right d-flex flex-column mt-5'>
                    <div>
                        <a href="https://play.google.com/store/apps/details?id=com.yourapp" className='border-4 border-light rounded-0 px-2 py-2 d-inline-flex align-items-center gap-2'>
                            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/7/78/Google_Play_Store_badge_EN.svg/1280px-Google_Play_Store_badge_EN.svg.png" height="60" />
                        </a>
                        <a href="https://play.google.com/store/apps/details?id=com.yourapp" className='border-4 border-light rounded-0 px-2 py-2 d-inline-flex align-items-center gap-2'>
                            <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/3c/Download_on_the_App_Store_Badge.svg/960px-Download_on_the_App_Store_Badge.svg.png" height="60" />
                        </a>
                    </div>

                    <div
                        style={{
                            display: "flex",
                            gap: "18px",
                            marginTop: "20px",
                            marginLeft: "10px"
                        }}
                    >
                        {socials.map((social, index) => (
                            <a
                                key={index}
                                href={social.url}
                                target="_blank"
                                style={{
                                    width: "48px",
                                    height: "48px",
                                    display: "flex",
                                    alignItems: "center",
                                    justifyContent: "center",
                                    background: social.bg,
                                    borderRadius: "50%",
                                    color: "white",
                                    fontSize: "22px",
                                    textDecoration: "none",
                                    transition: "all 0.3s ease"
                                }}
                                onMouseEnter={(e) => {
                                    e.currentTarget.style.transform = "scale(1.15)";
                                }}
                                onMouseLeave={(e) => {
                                    e.currentTarget.style.transform = "scale(1)";
                                }}
                            >
                                {social.icon}
                            </a>
                        ))}
                    </div>
                </div>


            </Container>
        </section>
    );
};

export default Hero;