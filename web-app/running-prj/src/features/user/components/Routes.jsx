import React, { useEffect, useState } from 'react';
import { Container, Card, Badge, Modal, Button } from 'react-bootstrap';
import { MapPin, X } from 'lucide-react';
import axiosClient from '@/shared/services/axiosClient';
import { MapContainer, TileLayer, Polyline, Marker } from 'react-leaflet';
import { decode } from 'google-polyline';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

// Fix icon lỗi hiển thị của Leaflet
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
    iconRetinaUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png",
    iconUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png",
    shadowUrl: "https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png",
});

const Routes = () => {
    const [routes, setRoutes] = useState([]);
    const [selectedRoute, setSelectedRoute] = useState(null); // Lưu route đang được chọn để xem bản đồ
    const [showMap, setShowMap] = useState(false);

    useEffect(() => {
        const fetchRoutes = async () => {
            try {
                const user = JSON.parse(localStorage.getItem("userInfo"));
                let res;
                if (user?.location) {
                    res = await axiosClient.get('/api/highlight-routes/by-location', {
                        params: { location: user.location, limit: 6 }
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

    // Hàm xử lý khi click vào Card
    const handleCardClick = (route) => {
        if (route.polyline) {
            setSelectedRoute(route);
            setShowMap(true);
        }
    };

    // Giải mã polyline thành mảng tọa độ [lat, lng]
    const getCoordinates = (polylineStr) => {
        try {
            return decode(polylineStr);
        } catch (e) {
            console.error("Polyline decode error:", e);
            return [];
        }
    };

    return (
        <section className="py-5 bg-light">
            <Container>
                <h2 className="fw-bold mb-4 border-start border-4 border-dark ps-3">
                    TUYẾN ĐƯỜNG NỔI BẬT
                </h2>

                <div className="route-scroll">
                    {routes.map((item, index) => (
                        <Card
                            key={index}
                            className="route-card"
                            onClick={() => handleCardClick(item)}
                        >
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
                                {item.polyline ? (
                                    <small className="text-primary">Bấm để xem bản đồ</small>
                                ) : (
                                    <small className="text-muted">Chưa có bản đồ</small>
                                )}
                            </Card.Body>
                        </Card>
                    ))}
                </div>
            </Container>

            {/* MODAL HIỂN THỊ BẢN ĐỒ */}
            <Modal show={showMap} onHide={() => setShowMap(false)} size="lg" centered>
                <Modal.Header closeButton>
                    <Modal.Title className="fw-bold">
                        {selectedRoute?.title}
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body style={{ height: '500px', padding: 0 }}>
                    {selectedRoute && selectedRoute.polyline && (
                        <MapContainer
                            center={getCoordinates(selectedRoute.polyline)[0]}
                            zoom={14}
                            style={{ height: '100%', width: '100%' }}
                        >
                            <TileLayer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
                            <Polyline
                                positions={getCoordinates(selectedRoute.polyline)}
                                color="red"
                                weight={5}
                            />
                            {/* Marker điểm đầu và điểm cuối */}
                            <Marker position={getCoordinates(selectedRoute.polyline)[0]} />
                            <Marker position={getCoordinates(selectedRoute.polyline).slice(-1)[0]} />
                        </MapContainer>
                    )}
                </Modal.Body>
                <Modal.Footer>
                    <div className="me-auto">
                        <MapPin size={16} /> <strong>{selectedRoute?.location}</strong> - {selectedRoute?.distanceLabel}
                    </div>
                    <Button variant="secondary" onClick={() => setShowMap(false)}>Đóng</Button>
                </Modal.Footer>
            </Modal>

            <style>{`
                .route-scroll {
                    display: flex;
                    overflow-x: auto;
                    gap: 20px;
                    padding: 10px 5px 20px 5px;
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
                    transform: translateY(-8px);
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