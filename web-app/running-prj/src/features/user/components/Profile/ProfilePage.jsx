import { AuthActionContext, AuthDataContext } from "@/app/providers/AuthProvider";
import React, { useContext, useEffect, useState } from "react";
import { Alert, Button, Card, Col, Container, Form, Row, Spinner } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { updateMyAvatar, updateMyProfile } from "@/features/admin/users/services/UserService";
import uploadFile from "@/shared/services/UploadService";

const ProfilePage = () => {
    const { user } = useContext(AuthDataContext);
    const { changeUser, logout } = useContext(AuthActionContext);
    const navigate = useNavigate();

    const [form, setForm] = useState({
        username: "",
        fullName: "",
        phoneNumber: "",
        address: "",
        latitude: "",
        longitude: "",
        avatar: "",
    });

    const [isEditing, setIsEditing] = useState(false);
    const [isSaving, setIsSaving] = useState(false);
    const [isGettingLocation, setIsGettingLocation] = useState(false);
    const [isUploadingAvatar, setIsUploadingAvatar] = useState(false);
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    useEffect(() => {
        if (!user) return;

        console.log("user ", user)

        setForm({
            username: user.username ?? "",
            fullName: user.fullName ?? "",
            phoneNumber: user.phoneNumber ?? "",
            address: user.address ?? "",
            latitude: user.latitude ?? "",
            longitude: user.longitude ?? "",
            avatar: user.imageUrl ?? "",
        });

        setIsEditing(false);
        setMessage("");
        setError("");
    }, [user]);

    const onChange = (e) => {
        const { name, value } = e.target;
        setForm((prev) => ({ ...prev, [name]: value }));
    };

    const getCurrentPosition = () => {
        if (!navigator.geolocation) {
            setError("Trình duyệt không hỗ trợ lấy vị trí hiện tại.");
            return;
        }

        setIsGettingLocation(true);
        setError("");
        setMessage("");

        navigator.geolocation.getCurrentPosition(
            (position) => {
                setForm((prev) => ({
                    ...prev,
                    latitude: String(position.coords.latitude),
                    longitude: String(position.coords.longitude),
                }));
                setMessage("Đã cập nhật tọa độ hiện tại vào form.");
                setIsGettingLocation(false);
            },
            () => {
                setError("Không thể lấy tọa độ hiện tại. Vui lòng kiểm tra quyền truy cập vị trí.");
                setIsGettingLocation(false);
            }
        );
    };

    const onCancel = () => {
        setForm({
            username: user?.username ?? "",
            fullName: user?.fullName ?? "",
            phoneNumber: user?.phoneNumber ?? "",
            address: user?.address ?? "",
            latitude: user?.latitude ?? "",
            longitude: user?.longitude ?? "",
            avatar: user?.imageUrl ?? "",
        });
        setIsEditing(false);
        setMessage("");
        setError("");
    };

    const onSave = async () => {
        try {
            setIsSaving(true);
            setMessage("");
            setError("");

            const payload = {
                fullName: form.fullName.trim(),
                phoneNumber: form.phoneNumber.trim(),
                address: form.address.trim(),
                latitude: form.latitude,
                longitude: form.longitude,
                imageUrl: form.avatar,
            };

            const response = await updateMyProfile(payload);
            const updatedUser = response?.data?.data;

            changeUser(updatedUser);
            setIsEditing(false);
            setMessage("Cập nhật hồ sơ thành công.");
        } catch (err) {
            setError(err?.response?.data?.message || "Cập nhật hồ sơ thất bại.");
        } finally {
            setIsSaving(false);
        }
    };

    const onAvatarChange = async (e) => {
        const file = e.target.files?.[0];
        if (!file) return;

        try {
            setIsUploadingAvatar(true);
            setError("");
            setMessage("");

            const imageUrl = await uploadFile(file, "AVATAR_USER");
            const response = await updateMyAvatar(imageUrl);
            const updatedUser = response?.data?.data;

            changeUser(updatedUser);
            setForm((prev) => ({
                ...prev,
                avatar: updatedUser?.imageUrl || imageUrl,
            }));
            setMessage("Cập nhật ảnh đại diện thành công.");
        } catch (err) {
            setError(err?.response?.data?.message || err?.message || "Cập nhật ảnh đại diện thất bại.");
        } finally {
            setIsUploadingAvatar(false);
            e.target.value = "";
        }
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
                            <Form.Group controlId="profileAvatarUpload" className="mb-3">
                                <Form.Label
                                    className="d-inline-block position-relative"
                                    style={{ cursor: isUploadingAvatar ? "wait" : "pointer" }}
                                >
                                    {form.avatar ? (
                                        <img
                                            src={form.avatar}
                                            alt="avatar"
                                            className="rounded-circle"
                                            style={{ width: 96, height: 96, objectFit: "cover" }}
                                        />
                                    ) : (
                                        <div
                                            className="rounded-circle d-flex align-items-center justify-content-center mx-auto"
                                            style={{
                                                width: 96,
                                                height: 96,
                                                background: "#6f4ef6",
                                                color: "white",
                                                fontSize: 30,
                                                fontWeight: 700,
                                            }}
                                        >
                                            {(user?.username || "U").slice(0, 1).toUpperCase()}
                                        </div>
                                    )}

                                    <div className="small text-muted mt-2">
                                        {isUploadingAvatar ? "Đang tải ảnh..." : "Bấm vào ảnh để cập nhật"}
                                    </div>
                                </Form.Label>

                                <Form.Control
                                    type="file"
                                    accept="image/*"
                                    onChange={onAvatarChange}
                                    disabled={isUploadingAvatar}
                                    style={{ display: "none" }}
                                />
                            </Form.Group>

                            <h5>{user?.fullName || user?.username}</h5>

                            <div className="text-muted mb-2">
                                {user?.username || "Chưa có email"}
                            </div>

                            <div className="text-muted mb-2">
                                📍 {user?.address || "Chưa có address"}
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
                                        <Button onClick={onSave} disabled={isSaving}>
                                            {isSaving ? "Đang lưu..." : "Lưu"}
                                        </Button>
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

                            {message && <Alert variant="success">{message}</Alert>}
                            {error && <Alert variant="danger">{error}</Alert>}

                            <Form>
                                <Row className="g-3">
                                    <Col md={6}>
                                        <Form.Group>
                                            <Form.Label>Email</Form.Label>
                                            <Form.Control
                                                name="username"
                                                value={form.username}
                                                disabled
                                            />
                                        </Form.Group>
                                    </Col>

                                    <Col md={6}>
                                        <Form.Group>
                                            <Form.Label>Họ và tên</Form.Label>
                                            <Form.Control
                                                name="fullName"
                                                value={form.fullName}
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
                                            <Form.Label>Address</Form.Label>
                                            <Form.Control
                                                name="address"
                                                value={form.address}
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
                                                readOnly
                                                disabled
                                            />
                                        </Form.Group>
                                    </Col>

                                    <Col md={3}>
                                        <Form.Group>
                                            <Form.Label>Longitude</Form.Label>
                                            <Form.Control
                                                name="longitude"
                                                value={form.longitude}
                                                readOnly
                                                disabled
                                            />
                                        </Form.Group>
                                    </Col>

                                    {isEditing && (
                                        <Col md={12}>
                                            <Button
                                                type="button"
                                                variant="outline-primary"
                                                onClick={getCurrentPosition}
                                                disabled={isGettingLocation}
                                            >
                                                {isGettingLocation ? (
                                                    <>
                                                        <Spinner
                                                            as="span"
                                                            animation="border"
                                                            size="sm"
                                                            className="me-2"
                                                        />
                                                        Đang lấy tọa độ...
                                                    </>
                                                ) : (
                                                    "Lấy tọa độ của bạn"
                                                )}
                                            </Button>
                                        </Col>
                                    )}

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
