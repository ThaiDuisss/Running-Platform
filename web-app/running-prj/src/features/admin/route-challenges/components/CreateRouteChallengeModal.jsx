import { Modal, Form, Button } from "react-bootstrap";
import { useState } from "react";

const CreateRouteChallengeModal = ({ show, onClose, onCreate }) => {
    const [formData, setFormData] = useState({
        title: "",
        description: "",
        type: "DISTANCE",
        targetValue: "",
        startTime: "",
        endTime: "",
        visibility: "PUBLIC",
        challengeType: "DISTANCE", // Note: Backend has both 'type' and 'challengeType', adjust if needed
    });

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onCreate(formData);
        setFormData({
            title: "",
            description: "",
            type: "DISTANCE",
            targetValue: "",
            startTime: "",
            endTime: "",
            visibility: "PUBLIC",
            challengeType: "DISTANCE",
        });
        onClose();
    };

    return (
        <Modal show={show} onHide={onClose}>
            <Modal.Header closeButton>
                <Modal.Title>Create Route Challenge</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Label>Title</Form.Label>
                        <Form.Control
                            type="text"
                            name="title"
                            value={formData.title}
                            onChange={handleChange}
                            required
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Description</Form.Label>
                        <Form.Control
                            as="textarea"
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Type</Form.Label>
                        <Form.Select name="type" value={formData.type} onChange={handleChange}>
                            <option value="DISTANCE">Distance</option>
                            <option value="RUN_COUNT">Run Count</option>
                        </Form.Select>
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Target Value</Form.Label>
                        <Form.Control
                            type="number"
                            name="targetValue"
                            value={formData.targetValue}
                            onChange={handleChange}
                            required
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Start Time</Form.Label>
                        <Form.Control
                            type="datetime-local"
                            name="startTime"
                            value={formData.startTime}
                            onChange={handleChange}
                            required
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>End Time</Form.Label>
                        <Form.Control
                            type="datetime-local"
                            name="endTime"
                            value={formData.endTime}
                            onChange={handleChange}
                            required
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Visibility</Form.Label>
                        <Form.Select name="visibility" value={formData.visibility} onChange={handleChange}>
                            <option value="PUBLIC">Public</option>
                            <option value="PRIVATE">Private</option>
                            <option value="FRIEND">Friend</option>
                        </Form.Select>
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Challenge Type</Form.Label>
                        <Form.Select name="challengeType" value={formData.challengeType} onChange={handleChange}>
                            <option value="DISTANCE">Distance</option>
                            <option value="RUN_COUNT">Run Count</option>
                        </Form.Select>
                    </Form.Group>
                    <Button variant="primary" type="submit">
                        Create
                    </Button>
                </Form>
            </Modal.Body>
        </Modal>
    );
};

export default CreateRouteChallengeModal;