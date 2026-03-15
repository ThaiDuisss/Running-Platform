import { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import { updateUserAPI } from '../services/UserService';

const UpdateUser = ({ show, selectedUser, onClose, fetchUsers }) => {

    const [phoneNumber, setPhoneNumber] = useState("");
    const [username, setUsername] = useState("");
    const [role, setRole] = useState("");
    const [avatar, setAvatar] = useState(null);
    const [preview, setPreview] = useState("");
    useEffect(() => {
        if (selectedUser) {
            setUsername(selectedUser.username || "");
            setPhoneNumber(selectedUser.phoneNumber || "");
            selectedUser.roles?.[0]?.roleName
            setPreview(selectedUser.imageUrl || "");

        }
    }, [selectedUser]);

    const handleSubmit = async () => {

        const formData = new FormData();

        formData.append("username", username);
        formData.append("phoneNumber", phoneNumber);
        formData.append("role", role);

        if (avatar) {
            formData.append("avatar", avatar);
        }

        try {

            const res = await updateUserAPI(formData, selectedUser.id);

            fetchUsers();
            onClose();

        } catch (error) {

            console.error(error);

        }
    };
    const handleAvatarChange = (e) => {
        const file = e.target.files[0];

        if (file) {
            setAvatar(file);

            const previewUrl = URL.createObjectURL(file);
            setPreview(previewUrl);
        }
    };
    return (
        <Modal show={show} onHide={onClose}>

            <Modal.Header closeButton>
            </Modal.Header>

            <Modal.Body>
                <div style={{ textAlign: "center", marginBottom: "20px" }}>
                    <label htmlFor="avatarUpload" style={{ cursor: "pointer" }}>
                        <img
                            src={preview || "https://via.placeholder.com/100"}
                            alt="avatar"
                            style={{
                                width: "100px",
                                height: "100px",
                                borderRadius: "50%",
                                objectFit: "cover",
                                border: "2px solid #ddd"
                            }}
                        />
                    </label>

                    <input
                        id="avatarUpload"
                        type="file"
                        accept="image/*"
                        style={{ display: "none" }}
                        onChange={handleAvatarChange}
                    />
                </div>
                <Form>

                    <Form.Group className="mb-3">
                        <Form.Label>Username</Form.Label>
                        <Form.Control
                            type="text"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </Form.Group>

                    <Form.Group className="mb-3">
                        <Form.Label>Phone Number</Form.Label>
                        <Form.Control
                            type="text"
                            value={phoneNumber}
                            onChange={(e) => setPhoneNumber(e.target.value)}
                        />
                    </Form.Group>

                    {/* <Form.Group className="mb-3">
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </Form.Group> */}

                    <Form.Group className="mb-3">
                        <Form.Label>Role</Form.Label>
                        <Form.Select
                            value={role}
                            onChange={(e) => setRole(e.target.value)}
                        >
                            <option value="">Select role</option>
                            <option value="ADMIN">Admin</option>
                            <option value="USER">User</option>
                        </Form.Select>
                    </Form.Group>

                </Form>

            </Modal.Body>

            <Modal.Footer>

                <Button variant="secondary" onClick={onClose}>
                    Cancel
                </Button>

                <Button variant="primary" onClick={handleSubmit}>
                    Update
                </Button>

            </Modal.Footer>

        </Modal>
    );
}

export default UpdateUser;