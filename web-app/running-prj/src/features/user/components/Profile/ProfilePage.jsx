import { AuthActionContext, AuthDataContext } from "@/app/providers/AuthProvider";
import React, { useContext, useEffect, useState } from "react";
import { Button, Card, Col, Container, Form, Row } from "react-bootstrap";
import { NavLink, useNavigate } from "react-router-dom";

const ProfilePage = () => {
    const { user } = useContext(AuthDataContext);
    const { changeUser, logout } = useContext(AuthActionContext);
    const navigate = useNavigate();

    const [form, setForm] = useState({
        username: "",
        phoneNumber: "",
        location: "",
        latitude: "",
        longitude: "",
        avatar: "",
    });

    const [isEditing, setIsEditing] = useState(false);

    // Sync form khi user thay đổi
    useEffect(() => {
        console.log("User data changed, updating form:", user.data);
        if (!user.data) return;
        setForm({
            username: user.data.username ?? "",
            phoneNumber: user.data.phoneNumber ?? "",
            location: user.data.location ?? "",
            latitude: user.data.latitude ?? "",
            longitude: user.data.longitude ?? "",
            avatar: user.data.avatar ?? "",
        });

        setIsEditing(false);
    }, [user]);

    // const getCurrentPossition = () => {
    //     if (!navigator.geolocation) {
    //         alert("Geolocation is not supported by your browser");
    //         return;
    //     }

    //     navigator.geolocation.getCurrentPosition(
    //         (position) => {
    //             setForm((prev) => ({
    //                 ...prev,
    //                 latitude: position.coords.latitude,
    //                 longitude: position.coords.longitude,
    //             }));
    //         },
    //         (error) => {
    //             console.error("Error getting location:", error);
    //             alert("Unable to retrieve your location");
    //         }
    //     );
    // };

    const onChange = (e) => {
        const { name, value } = e.target;
        setForm((prev) => ({ ...prev, [name]: value }));
    };

    const onCancel = () => {
        setForm({
            username: user?.username ?? "",
            phoneNumber: user?.phoneNumber ?? "",
            location: user?.location ?? "",
            latitude: user?.latitude ?? "",
            longitude: user?.longitude ?? "",
            avatar: user?.avatar ?? "",
        });
        setIsEditing(false);
    };

    const onSave = () => {
        const nextUser = {
            ...user,
            username: form.username.trim(),
            phoneNumber: form.phoneNumber.trim(),
            location: form.location.trim(),
            latitude: form.latitude,
            longitude: form.longitude,
            avatar: form.avatar,
        };

        changeUser(nextUser);
        setIsEditing(false);
    };

    const onLogout = () => {
        logout();
        navigate("/", { replace: true });
    };

    return (
        <Container className="py-4">
            <Row className="g-3">
                {/* PROFILE CARD */}
                <Col lg={4}>
                    <Card className="shadow-sm">
                        <Card.Body className="p-4 text-center">
                            {user?.avatar ? (
                                <img
                                    src={user.avatar}
                                    alt="avatar"
                                    className="rounded-circle mb-3"
                                    style={{ width: 80, height: 80, objectFit: "cover" }}
                                />
                            ) : (
                                <div
                                    className="rounded-circle d-flex align-items-center justify-content-center mx-auto mb-3"
                                    style={{
                                        width: 80,
                                        height: 80,
                                        background: "#6f4ef6",
                                        color: "white",
                                        fontSize: 26,
                                        fontWeight: 700,
                                    }}
                                >
                                    {(user?.username || "U").slice(0, 1).toUpperCase()}
                                </div>
                            )}

                            <h5>{user?.username}</h5>

                            <div className="text-muted mb-2">
                                📍 {user?.location || "Chưa có location"}
                            </div>

                            <div className="text-muted mb-3">
                                📞 {user?.phoneNumber || "Chưa có số điện thoại"}
                            </div>

                            {/* ROLE */}
                            <div className="mb-2">
                                {user?.roles?.map((r) => (
                                    <span key={r.id} className="badge bg-primary me-1">
                                        {r.name}
                                    </span>
                                ))}
                            </div>

                            {/* VIP */}
                            <div className="text-muted mb-3">
                                VIP đến:{" "}
                                {user?.vipExpiredAt
                                    ? new Date(user.vipExpiredAt).toLocaleDateString()
                                    : "Free"}
                            </div>

                            <div className="d-grid gap-2">
                                {!isEditing ? (
                                    <Button onClick={() => setIsEditing(true)}>
                                        Chỉnh sửa
                                    </Button>
                                ) : (
                                    <>
                                        <Button onClick={onSave}>Lưu</Button>
                                        <Button variant="outline-secondary" onClick={onCancel}>
                                            Hủy
                                        </Button>
                                    </>
                                )}

                                <Button variant="outline-danger" onClick={onLogout}>
                                    Đăng xuất
                                </Button>
                            </div>
                        </Card.Body>
                    </Card>
                </Col>

                {/* FORM */}
                <Col lg={8}>
                    <Card className="shadow-sm">
                        <Card.Body className="p-4">
                            <h4 className="mb-3">Thông tin cá nhân</h4>

                            <Form>
                                <Row className="g-3">
                                    <Col md={6}>
                                        <Form.Group>
                                            <Form.Label>Username</Form.Label>
                                            <Form.Control
                                                name="username"
                                                value={form.username}
                                                onChange={onChange}
                                                disabled={!isEditing}
                                            />
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group>
                                            <Form.Label>Số điện thoại</Form.Label>
                                            <Form.Control
                                                name="phoneNumber"
                                                value={form.phoneNumber}
                                                onChange={onChange}
                                                disabled={!isEditing}
                                            />
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group>
                                            <Form.Label>Location</Form.Label>
                                            <Form.Control
                                                name="location"
                                                value={form.location}
                                                onChange={onChange}
                                                disabled={!isEditing}
                                            />
                                        </Form.Group>
                                    </Col>

                                    <Col md={3}>
                                        <Form.Group>
                                            <Form.Label>Latitude</Form.Label>
                                            <Form.Control
                                                name="latitude"
                                                value={form.latitude}
                                                onChange={onChange}
                                                disabled={!isEditing}
                                            />
                                        </Form.Group>
                                    </Col>

                                    <Col md={3}>
                                        <Form.Group>
                                            <Form.Label>Longitude</Form.Label>
                                            <Form.Control
                                                name="longitude"
                                                value={form.longitude}
                                                onChange={onChange}
                                                disabled={!isEditing}
                                            />
                                        </Form.Group>
                                    </Col>

                                    {/* <Col md={12}>
                      <Form.Group>
                        <Form.Label>Avatar URL</Form.Label>
                        <Form.Control
                          name="avatar"
                          value={form.avatar}
                          onChange={onChange}
                          disabled={!isEditing}
                        />
                      </Form.Group>
                    </Col> */}
                                </Row>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default ProfilePage;