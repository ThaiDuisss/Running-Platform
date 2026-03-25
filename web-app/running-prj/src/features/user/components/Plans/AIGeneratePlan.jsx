import React, { useState } from "react";
import { Modal, Form, Button } from "react-bootstrap";

const AIGeneratePlan = ({ show, onHide, onSubmit }) => {
    const [formData, setFormData] = useState({
        primaryGoal: "Build Endurance",
        experienceLevel: "Intermediate",
        frequency: 3,
        timePreference: "Morning",
        maxDistance: 5,
        injuriesLimitations: "None",
        startDate: new Date().toISOString().split('T')[0]
    });

    const goals = ["Lose Weight", "Build Endurance", "5K Training"];
    const levels = ["Beginner", "Intermediate", "Advanced"];
    const times = ["Morning", "Afternoon", "Evening"];
    const injuries = ["None", "Knee Pain", "Ankle Issue", "Back Pain"];

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
    };

    return (
        <Modal show={true} onHide={onHide} size="lg" centered backdrop="static"
            contentClassName="bg-dark text-white"
        >
            <Modal.Header closeButton className="border-bottom border-secondary">
                <Modal.Title>✨ AI Generate Plan</Modal.Title>
            </Modal.Header>
            <Modal.Body style={{ maxHeight: "80vh", overflowY: "auto" }}>
                <Form onSubmit={handleSubmit}>
                    {/* Primary Goal */}
                    <Form.Group className="mb-4">
                        <Form.Label className="fw-bold" style={{ color: "#8b5cf6" }}>
                            Primary Goal
                        </Form.Label>
                        <Form.Select
                            value={formData.primaryGoal}
                            onChange={(e) => setFormData(prev => ({
                                ...prev,
                                primaryGoal: e.target.value
                            }))}
                            style={{
                                backgroundColor: "#2a2a45",
                                borderColor: "#4a4a65",
                                color: "#fff",
                                borderRadius: "8px"
                            }}
                        >
                            {goals.map(goal => (
                                <option key={goal} value={goal}>{goal}</option>
                            ))}
                        </Form.Select>
                    </Form.Group>

                    {/* Experience Level */}
                    <Form.Group className="mb-4">
                        <Form.Label className="fw-bold" style={{ color: "#8b5cf6" }}>
                            Experience Level
                        </Form.Label>
                        <Form.Select
                            value={formData.experienceLevel}
                            onChange={(e) => setFormData(prev => ({
                                ...prev,
                                experienceLevel: e.target.value
                            }))}
                            style={{
                                backgroundColor: "#2a2a45",
                                borderColor: "#4a4a65",
                                color: "#fff",
                                borderRadius: "8px"
                            }}
                        >
                            {levels.map(level => (
                                <option key={level} value={level}>{level}</option>
                            ))}
                        </Form.Select>
                    </Form.Group>

                    {/* Frequency */}
                    <Form.Group className="mb-4">
                        <Form.Label className="fw-bold" style={{ color: "#8b5cf6" }}>
                            Frequency (Days/Week)
                        </Form.Label>
                        <Form.Control
                            type="number"
                            min="1"
                            max="7"
                            value={formData.frequency}
                            onChange={(e) => setFormData(prev => ({
                                ...prev,
                                frequency: parseInt(e.target.value)
                            }))}
                            style={{
                                backgroundColor: "#2a2a45",
                                borderColor: "#4a4a65",
                                color: "#fff",
                                borderRadius: "8px"
                            }}
                        />
                    </Form.Group>

                    {/* Time Preference */}
                    <Form.Group className="mb-4">
                        <Form.Label className="fw-bold" style={{ color: "#8b5cf6" }}>
                            Time Preference
                        </Form.Label>
                        <Form.Select
                            value={formData.timePreference}
                            onChange={(e) => setFormData(prev => ({
                                ...prev,
                                timePreference: e.target.value
                            }))}
                            style={{
                                backgroundColor: "#2a2a45",
                                borderColor: "#4a4a65",
                                color: "#fff",
                                borderRadius: "8px"
                            }}
                        >
                            {times.map(time => (
                                <option key={time} value={time}>{time}</option>
                            ))}
                        </Form.Select>
                    </Form.Group>

                    {/* Max Distance */}
                    <Form.Group className="mb-4">
                        <div className="d-flex justify-content-between align-items-center mb-2">
                            <Form.Label className="fw-bold" style={{ color: "#8b5cf6", marginBottom: 0 }}>
                                Max Distance (km)
                            </Form.Label>
                            <span style={{
                                backgroundColor: "#667eea",
                                color: "#fff",
                                padding: "4px 12px",
                                borderRadius: "20px",
                                fontSize: "14px"
                            }}>
                                {formData.maxDistance} km
                            </span>
                        </div>
                        <input
                            type="range"
                            min="1"
                            max="42"
                            value={formData.maxDistance}
                            onChange={(e) => setFormData(prev => ({
                                ...prev,
                                maxDistance: parseInt(e.target.value)
                            }))}
                            className="form-range"
                            style={{
                                accentColor: "#667eea"
                            }}
                        />
                    </Form.Group>

                    {/* Injuries/Limitations */}
                    <Form.Group className="mb-4">
                        <Form.Label className="fw-bold" style={{ color: "#8b5cf6" }}>
                            Injuries/Limitations
                        </Form.Label>
                        <Form.Select
                            value={formData.injuriesLimitations}
                            onChange={(e) => setFormData(prev => ({
                                ...prev,
                                injuriesLimitations: e.target.value
                            }))}
                            style={{
                                backgroundColor: "#2a2a45",
                                borderColor: "#4a4a65",
                                color: "#fff",
                                borderRadius: "8px"
                            }}
                        >
                            {injuries.map(injury => (
                                <option key={injury} value={injury}>{injury}</option>
                            ))}
                        </Form.Select>
                    </Form.Group>

                    {/* Start Date */}
                    <Form.Group className="mb-4">
                        <Form.Label className="fw-bold" style={{ color: "#8b5cf6" }}>
                            Start Date
                        </Form.Label>
                        <Form.Control
                            type="date"
                            value={formData.startDate}
                            onChange={(e) => setFormData(prev => ({
                                ...prev,
                                startDate: e.target.value
                            }))}
                            style={{
                                backgroundColor: "#2a2a45",
                                borderColor: "#4a4a65",
                                color: "#fff",
                                borderRadius: "8px"
                            }}
                        />
                    </Form.Group>

                    <Button
                        type="submit"
                        className="w-100 py-3 fw-bold"
                        style={{
                            background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
                            border: "none",
                            borderRadius: "12px",
                            fontSize: "16px",
                            color: "white"
                        }}
                    >
                        ✨ Generate Schedule
                    </Button>
                </Form>
            </Modal.Body>
        </Modal>
    );
};

export default AIGeneratePlan;
