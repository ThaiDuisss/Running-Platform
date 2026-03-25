import React, { useState, useEffect } from "react";
import { Modal, Button } from "react-bootstrap";
import * as Icons from "lucide-react";
import "@/style/customPlanModal.css"
import { TRAINING_TYPES } from "@/shared/constant/constants";
export default function EditModal({ show, day, onClose, onSave }) {
    const [form, setForm] = useState(day);

    useEffect(() => {
        setForm(day);
    }, [day]);

    if (!form) return null;

    const handleSelectType = (type) => {
        setForm({ ...form, type });
    };

    const changeValue = (field, delta) => {
        setForm({
            ...form,
            [field]: Math.max(0, (form[field] || 0) + delta)
        });
    };

    return (
        <Modal show={show} onHide={onClose} centered>
            <div className="custom-modal">
                {/* HEADER */}
                <div className="modal-header-custom">
                    <div>
                        <h3>Edit {form.day}</h3>
                        <p>Customize your workout for this day.</p>
                    </div>
                    <span className="close-btn" onClick={onClose}>×</span>
                </div>

                {/* TRAINING TYPE */}
                <div className="section">
                    <p className="section-title">TRAINING TYPE</p>

                    <div className="type-grid">
                        {Object.entries(TRAINING_TYPES).map(([key, val]) => {
                            const Icon = Icons[val.icon];
                            const active = form.type === key;

                            return (
                                <div
                                    key={key}
                                    className={`type-card ${active ? "active" : ""}`}
                                    onClick={() => handleSelectType(key)}
                                >
                                    {Icon && <Icon size={20} />}
                                    <span>{val.label}</span>
                                </div>
                            );
                        })}
                    </div>
                </div>

                {/* DISTANCE + DURATION */}
                <div className="row-section">
                    <div>
                        <p className="section-title">DISTANCE (KM)</p>
                        <div className="counter">
                            <button onClick={() => changeValue("targetDistance", -1)}>-</button>
                            <span>{form.targetDistance || 0}</span>
                            <button onClick={() => changeValue("targetDistance", 1)}>+</button>
                        </div>
                    </div>

                    <div>
                        <p className="section-title">DURATION (MIN)</p>
                        <div className="counter">
                            <button onClick={() => changeValue("duration", -5)}>-</button>
                            <span>{form.duration || 0}</span>
                            <button onClick={() => changeValue("duration", 5)}>+</button>
                        </div>
                    </div>
                </div>

                {/* SAVE */}
                <Button className="save-btn" onClick={() => onSave(form)}>
                    Save Changes
                </Button>
            </div>
        </Modal>
    );
}