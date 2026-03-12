import { useState, useEffect } from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import Form from 'react-bootstrap/Form';
import { updateUserAPI } from '../services/UserService';

const UpdateUser = ({ show, selectedUser, onClose, fetchUsers }) => {

    const [phoneNumber, setPhoneNumber] = useState("");
    const [username, setUsername] = useState("");
    const [role, setRole] = useState("");

    useEffect(() => {
        if (selectedUser) {
            setUsername(selectedUser.username || "");
            setPhoneNumber(selectedUser.phoneNumber || "");
            setRole(selectedUser.roles?.roleName || "");
        }
    }, [selectedUser]);

    const handleSubmit = async () => {
        try {
            const data = {
                username,
                phoneNumber,
                role
            };
            const res = await updateUserAPI(data, selectedUser.id)
            console.log("Update user:", res);
            fetchUsers()
            onClose()
        } catch (error) {
            console.error(error.message)
        }
    };

    return (
        <Modal show={show} onHide={onClose}>

            <Modal.Header closeButton>
                <Modal.Title>Update User</Modal.Title>
            </Modal.Header>

            <Modal.Body>

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