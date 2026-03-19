import React, { useState } from "react";
import { Modal, Form, Button, Row, Col } from "react-bootstrap";

const CustomPlanModal = ({ show, onHide, onSubmit }) => {
    const [formData, setFormData] = useState({
        title: "Custom Plan",
        availableDays: [],
        injuryStatus: "No Injury",
        primaryGoal: "Build Endurance",
        experienceLevel: "Intermediate",
        timePreference: "Morning",
        maxDistance: 5,
        startDate: new Date().toISOString().split('T')[0]
    });

    const days = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
    const injuries = ["No Injury", "Knee Pain", "Ankle Issue", "Back Pain"];
    const goals = ["Lose Weight", "Build Endurance", "5K Training"];
    const levels = ["Beginner", "Intermediate", "Advanced"];
    const times = ["Morning", "Afternoon", "Evening"];

    const handleDayToggle = (day) => {
        setFormData(prev => ({
            ...prev,
            availableDays: prev.availableDays.includes(day)
                ? prev.availableDays.filter(d => d !== day)
                : [...prev.availableDays, day]
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
    };

    return (
        <Modal show={show} onHide={onHide} size="lg" centered backdrop="static"
            contentClassName="bg-dark text-white" 
            style={{ backgroundColor: "#1a1a2e" }}
        >
            <Modal.Header closeButton className="border-bottom border-secondary">
                <Modal.Title>Create Your Perfect Plan</Modal.Title>
            </Modal.Header>
            <Modal.Body style={{ maxHeight: "80vh", overflowY: "auto" }}>
                <Form onSubmit={handleSubmit}>
                    {/* Available Days */}
                    <div className="mb-4">
                        <label className="form-label fw-bold" style={{ color: "#8b5cf6" }}>
                            📅 Available Days - When do you want to train?
                        </label>
                        <div className="d-flex flex-wrap gap-2">
                            {days.map(day => (
                                <Button
                                    key={day}
                                    onClick={() => handleDayToggle(day)}
                                    variant={formData.availableDays.includes(day) ? "primary" : "outline-secondary"}
                                    style={{
                                        backgroundColor: formData.availableDays.includes(day) ? "#667eea" : "transparent",
                                        borderColor: "#667eea",
                                        color: "#fff",
                                        borderRadius: "8px",
                                        padding: "8px 16px",
                                        fontSize: "14px"
                                    }}
                                >
                                    {day}
                                </Button>
                            ))}
                        </div>
                    </div>

                    {/* Injury Status */}
                    <div className="mb-4">
                        <label className="form-label fw-bold" style={{ color: "#8b5cf6" }}>
                            ⚡ Injury Status - Safety first, always.
                        </label>
                        <div className="d-grid gap-2">
                            {injuries.map(injury => (
                                <Button
                                    key={injury}
                                    onClick={() => setFormData(prev => ({ ...prev, injuryStatus: injury }))}
                                    variant={formData.injuryStatus === injury ? "primary" : "outline-secondary"}
                                    style={{
                                        backgroundColor: formData.injuryStatus === injury ? "#667eea" : "transparent",
                                        borderColor: "#667eea",
                                        color: "#fff",
                                        borderRadius: "8px",
                                        padding: "12px",
                                        textAlign: "left"
                                    }}
                                >
                                    {formData.injuryStatus === injury && "✓ "}{injury}
                                </Button>
                            ))}
                        </div>
                    </div>

                    {/* Primary Goal */}
                    <div className="mb-4">
                        <label className="form-label fw-bold" style={{ color: "#8b5cf6" }}>
                            🎯 Primary Goal - What's your main objective?
                        </label>
                        <div className="d-grid gap-2">
                            {goals.map(goal => (
                                <Button
                                    key={goal}
                                    onClick={() => setFormData(prev => ({ ...prev, primaryGoal: goal }))}
                                    variant={formData.primaryGoal === goal ? "primary" : "outline-secondary"}
                                    style={{
                                        backgroundColor: formData.primaryGoal === goal ? "#667eea" : "transparent",
                                        borderColor: "#667eea",
                                        color: "#fff",
                                        borderRadius: "8px",
                                        padding: "12px",
                                        textAlign: "left"
                                    }}
                                >
                                    {formData.primaryGoal === goal && "✓ "}{goal}
                                </Button>
                            ))}
                        </div>
                    </div>

                    {/* Experience Level */}
                    <div className="mb-4">
                        <label className="form-label fw-bold" style={{ color: "#8b5cf6" }}>
                            ⚡ Experience Level - Be honest with yourself.
                        </label>
                        <div className="d-flex flex-wrap gap-2">
                            {levels.map(level => (
                                <Button
                                    key={level}
                                    onClick={() => setFormData(prev => ({ ...prev, experienceLevel: level }))}
                                    variant={formData.experienceLevel === level ? "primary" : "outline-secondary"}
                                    style={{
                                        backgroundColor: formData.experienceLevel === level ? "#667eea" : "transparent",
                                        borderColor: "#667eea",
                                        color: "#fff",
                                        borderRadius: "8px",
                                        padding: "8px 16px"
                                    }}
                                >
                                    {level}
                                </Button>
                            ))}
                        </div>
                    </div>

                    {/* Time Preference */}
                    <div className="mb-4">
                        <label className="form-label fw-bold" style={{ color: "#8b5cf6" }}>
                            🕐 Time Preference - When do you feel most energetic?
                        </label>
                        <div className="d-flex flex-wrap gap-2">
                            {times.map(time => (
                                <Button
                                    key={time}
                                    onClick={() => setFormData(prev => ({ ...prev, timePreference: time }))}
                                    variant={formData.timePreference === time ? "primary" : "outline-secondary"}
                                    style={{
                                        backgroundColor: formData.timePreference === time ? "#667eea" : "transparent",
                                        borderColor: "#667eea",
                                        color: "#fff",
                                        borderRadius: "8px",
                                        padding: "8px 16px"
                                    }}
                                >
                                    {time}
                                </Button>
                            ))}
                        </div>
                    </div>

                    {/* Max Distance with Slider */}
                    <div className="mb-4">
                        <div className="d-flex justify-content-between align-items-center mb-2">
                            <label className="form-label fw-bold" style={{ color: "#8b5cf6", marginBottom: 0 }}>
                                📍 Max Distance
                            </label>
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
                        <div className="d-flex justify-content-between mt-2" style={{ fontSize: "12px", color: "#999" }}>
                            <span>1 KM</span>
                            <span>21 KM</span>
                            <span>42 KM</span>
                        </div>
                    </div>

                    {/* Start Date */}
                    <div className="mb-4">
                        <label className="form-label fw-bold" style={{ color: "#8b5cf6" }}>
                            📅 Start Date - When should the journey begin?
                        </label>
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
                                borderRadius: "8px",
                                padding: "12px"
                            }}
                        />
                    </div>

                    {/* Plan Summary */}
                    <div className="mb-4" style={{
                        backgroundColor: "#2a2a45",
                        borderRadius: "12px",
                        padding: "20px",
                        border: "1px solid #4a4a65"
                    }}>
                        <label className="d-flex align-items-center gap-2 fw-bold mb-3" style={{ color: "#8b5cf6" }}>
                            <span>⏱️</span> PLAN SUMMARY
                        </label>
                        <div className="row">
                            <div className="col-6 mb-3">
                                <small style={{ color: "#999" }}>TRAINING DAYS</small>
                                <div style={{ color: "#fff", fontWeight: "bold" }}>
                                    {formData.availableDays.length > 0 
                                        ? formData.availableDays.join(", ")
                                        : "Choose days"}
                                </div>
                            </div>
                            <div className="col-6 mb-3">
                                <small style={{ color: "#999" }}>MAX DISTANCE</small>
                                <div style={{ color: "#fff", fontWeight: "bold" }}>
                                    {formData.maxDistance} km / week
                                </div>
                            </div>
                            <div className="col-6">
                                <small style={{ color: "#999" }}>EXPERIENCE</small>
                                <div style={{ color: "#fff", fontWeight: "bold" }}>
                                    {formData.experienceLevel}
                                </div>
                            </div>
                            <div className="col-6">
                                <small style={{ color: "#999" }}>PRIMARY GOAL</small>
                                <div style={{ color: "#fff", fontWeight: "bold" }}>
                                    {formData.primaryGoal}
                                </div>
                            </div>
                        </div>
                    </div>

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
                        🚀 Generate My Plan
                    </Button>
                </Form>
            </Modal.Body>
        </Modal>
    );
};

export default CustomPlanModal;
