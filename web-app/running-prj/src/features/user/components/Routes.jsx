import React, { useEffect, useState } from 'react';
import { Container, Card, Badge } from 'react-bootstrap';
import { MapPin } from 'lucide-react';
import axiosClient from '@/shared/services/axiosClient';

const Routes = () => {
    const [routes, setRoutes] = useState([]);

    useEffect(() => {
        const fetchRoutes = async () => {
            try {
                const user = JSON.parse(localStorage.getItem("userInfo"));

                let res;

                if (user?.location) {
                    res = await axiosClient.get('/api/highlight-routes/by-location', {
                        params: {
                            location: user.location,
                            limit: 6
                        }
                    });
                } else {
                    res = await axiosClient.get('/api/highlight-routes');
                }

                setRoutes(res.data.data || []);

            } catch (err) {
                console.error(err);
            }
        };

        fetchRoutes();
    }, []);

    return (
        <section className="py-5 bg-light">
            <Container>
                <h2 className="fw-bold mb-4 border-start border-4 border-dark ps-3">
                    TUYẾN ĐƯỜNG NỔI BẬT
                </h2>

                <div className="route-scroll">
                    {routes.map((item, index) => (
                        <Card key={index} className="route-card">
                            <div className="image-wrapper">
                                <Card.Img
                                    src={item.thumbnail}
                                    className="route-image"
                                />
                            </div>

                            <Card.Body>
                                <div className="d-flex justify-content-between align-items-center">
                                    <Badge bg="dark">{item.distanceLabel}</Badge>

                                    <span className="text-muted small">
                                        <MapPin size={14} /> {item.location}
                                    </span>
                                </div>

                                <h5 className="fw-bold mt-2">{item.title}</h5>
                            </Card.Body>
                        </Card>
                    ))}
                </div>

            </Container>

            {/* CSS */}
            <style>{`
                .route-scroll {
                    display: flex;
                    overflow-x: auto;
                    gap: 20px;
                    padding-bottom: 10px;
                }

                .route-card {
                    min-width: 400px;
                    border: none;
                    border-radius: 16px;
                    overflow: hidden;
                    flex: 0 0 auto;
                    transition: all 0.3s ease;
                    cursor: pointer;
                    box-shadow: 0 4px 12px rgba(0,0,0,0.08);
                }

                .route-card:hover {
                    transform: translateY(-8px) scale(1.03);
                    box-shadow: 0 12px 30px rgba(0,0,0,0.15);
                }

                .image-wrapper {
                    overflow: hidden;
                    height: 250px;
                }

                .route-image {
                    height: 100%;
                    width: 100%;
                    object-fit: cover;
                    transition: transform 0.4s ease;
                }

                .route-card:hover .route-image {
                    transform: scale(1.1);
                }

                .route-scroll::-webkit-scrollbar {
                    height: 6px;
                }

                .route-scroll::-webkit-scrollbar-thumb {
                    background: #ccc;
                    border-radius: 10px;
                }
            `}</style>
        </section>
    );
};

export default Routes;