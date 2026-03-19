import React, { useState, useEffect } from "react";
import { Container, Row, Col } from "react-bootstrap";
import { FaChevronLeft, FaChevronRight, FaMagic, FaPlus } from "react-icons/fa";
import { planService } from "@/app/services/PlanService";
import InstanceWorkoutDay from "./InstanceWorkoutDay";
import "@/style/plan.css";

const PlanPage = () => {
    const now = new Date();

    const [date, setDate] = useState(new Date(now.getFullYear(), now.getMonth(), 1));
    const [plans, setPlans] = useState([]);
    const [loading, setLoading] = useState(false);

    const [showCRUD, setShowCRUD] = useState(false);
    const [selectedDate, setSelectedDate] = useState(null);
    const [selectedPlan, setSelectedPlan] = useState(null);

    const [showAI, setShowAI] = useState(false);
    const [showCustom, setShowCustom] = useState(false);

    useEffect(() => {
        fetchPlans();
    }, [date]);

    const fetchPlans = async () => {
        setLoading(true);
        try {
            const res = await planService.getPlansByMonth(
                date.getFullYear(),
                date.getMonth() + 1
            );
            const data = res.data || res;
            setPlans(Array.isArray(data) ? data : []);
        } catch (e) {
            console.error(e);
        } finally {
            setLoading(false);
        }
    };

    const getDaysInMonth = () =>
        new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate();

    const getPlansForDate = (day) => {
        const target = new Date(date.getFullYear(), date.getMonth(), day)
            .toISOString().split("T")[0];

        return plans.filter(p =>
            new Date(p.scheduledDate).toISOString().split("T")[0] === target
        );
    };

    const handleSubmit = async (data) => {
        const payload = {
            ...data,
            scheduledDate: selectedDate.toISOString().split("T")[0]
        };

        try {
            if (selectedPlan?.id) {
                await planService.updatePlan(selectedPlan.id, payload);
            } else {
                await planService.createPlan(payload);
            }

            setShowCRUD(false);
            fetchPlans();
        } catch (e) {
            console.error(e);
        }
    };

    return (
        <Container fluid className="plan-container">

            {/* HEADER */}
            <div className="plan-header">
                <h2>Training Schedule</h2>

                <div className="month-nav">
                    <FaChevronLeft onClick={() =>
                        setDate(new Date(date.getFullYear(), date.getMonth() - 1, 1))
                    } />
                    <span>
                        {date.toLocaleDateString("en-US", { month: "long", year: "numeric" })}
                    </span>
                    <FaChevronRight onClick={() =>
                        setDate(new Date(date.getFullYear(), date.getMonth() + 1, 1))
                    } />
                </div>
            </div>

            {/* AI + CUSTOM */}
            <Row className="mb-4 g-3">
                <Col md={6}>
                    <div className="plan-card ai" onClick={() => setShowAI(true)}>
                        <FaMagic size={22} />
                        <h4>AI Generate Plan</h4>
                        <p>Let AI build your schedule</p>
                    </div>
                </Col>

                <Col md={6}>
                    <div className="plan-card custom" onClick={() => setShowCustom(true)}>
                        <FaPlus size={22} />
                        <h4>Custom Plan</h4>
                        <p>Create your own workouts</p>
                    </div>
                </Col>
            </Row>

            {/* LIST */}
            <div className="calendar-list">
                {Array.from({ length: getDaysInMonth() }, (_, i) => i + 1).map(day => {
                    const dayPlans = getPlansForDate(day);
                    const dayDate = new Date(date.getFullYear(), date.getMonth(), day);

                    return (
                        <div key={day} className="calendar-row">

                            <div className="calendar-date">
                                <span className="day-name">
                                    {dayDate.toLocaleDateString("en-US", { weekday: "short" })}
                                </span>
                                <span className="day-number">{day}</span>
                            </div>

                            <div className="day-workouts-scroll">
                                {dayPlans.length > 0 ? (
                                    dayPlans.map(plan => (
                                        <InstanceWorkoutDay
                                            key={plan.id}
                                            plan={plan}
                                            onEdit={() => {
                                                setSelectedPlan(plan);
                                                setSelectedDate(dayDate);
                                                setShowCRUD(true);
                                            }}
                                            onDelete={() =>
                                                planService.deletePlan(plan.id).then(fetchPlans)
                                            }
                                        />
                                    ))
                                ) : (
                                    <div
                                        className="empty-workout"
                                        onClick={() => {
                                            setSelectedPlan(null);
                                            setSelectedDate(dayDate);
                                            setShowCRUD(true);
                                        }}
                                    >
                                        + Add workout
                                    </div>
                                )}
                            </div>
                        </div>
                    );
                })}
            </div>

            {/* CRUD MODAL */}
            {showCRUD && (
                <ModalUI title="Workout" onClose={() => setShowCRUD(false)}>
                    <FormUI onSubmit={handleSubmit} data={selectedPlan} />
                </ModalUI>
            )}

            {/* AI MODAL */}
            {showAI && (
                <ModalUI title="AI Generate Plan" onClose={() => setShowAI(false)}>
                    <input className="input" placeholder="Goal..." />
                    <input className="input" placeholder="Level..." />
                    <button className="btn">Generate</button>
                </ModalUI>
            )}

            {/* CUSTOM MODAL */}
            {showCustom && (
                <ModalUI title="Custom Plan" onClose={() => setShowCustom(false)}>
                    <input className="input" placeholder="Plan name" />
                    <input className="input" type="date" />
                    <button className="btn">Create</button>
                </ModalUI>
            )}

        </Container>
    );
};



// ===== FORM =====
const FormUI = ({ onSubmit, data }) => {
    const [form, setForm] = useState({
        title: data?.title || "",
        targetDistance: data?.targetDistance || 0,
        duration: data?.duration || 30,
        setTime: data?.setTime || false,
        startTime: data?.startTime || { hour: 6, minute: 0 },
        endTime: data?.endTime || { hour: 7, minute: 0 }
    });

    const formatTime = (h, m) => {
        const ampm = h >= 12 ? "PM" : "AM";
        const h12 = h % 12 || 12;
        return `${h12}:${String(m).padStart(2, "0")} ${ampm}`;
    };

    const toTimeString = (t) => {
    return `${String(t.hour).padStart(2, "0")}:${String(t.minute).padStart(2, "0")}:00`;
};


const handleSubmit = () => {
    onSubmit({
        title: form.title,
        targetDistance: form.targetDistance,
        duration: form.duration,
        setTime: form.setTime,

        startTime: form.setTime ? toTimeString(form.startTime) : null,
        endTime: form.setTime ? toTimeString(form.endTime) : null,

        completed: false
    });
};

    return (
        <div className="activity-form">

            <div className="activity-card">
                <h3>{form.title || "Workout"}</h3>
                <div className="activity-meta">
                    {form.targetDistance} KM • {form.duration} min
                </div>
            </div>

            <input
                className="input"
                value={form.title}
                onChange={(e) => setForm({ ...form, title: e.target.value })}
                placeholder="Workout title"
            />

            <input
                className="input"
                type="number"
                value={form.targetDistance}
                onChange={(e) => setForm({ ...form, targetDistance: Number(e.target.value) })}
                placeholder="Distance"
            />

            <input
                className="input"
                type="number"
                value={form.duration}
                onChange={(e) => setForm({ ...form, duration: Number(e.target.value) })}
                placeholder="Duration"
            />

            <div className="switch-row">
                <span>Schedule Time</span>
                <input
                    type="checkbox"
                    checked={form.setTime}
                    onChange={() => setForm({ ...form, setTime: !form.setTime })}
                />
            </div>

            {form.setTime && (
                <div className="time-box">

                    <div className="time-row">
                        <span>Start</span>
                        <strong>{formatTime(form.startTime.hour, form.startTime.minute)}</strong>
                    </div>

                    <input
                        type="range"
                        min="0"
                        max="1439"
                        value={form.startTime.hour * 60 + form.startTime.minute}
                        onChange={(e) => {
                            const t = Number(e.target.value);
                            setForm({
                                ...form,
                                startTime: { hour: Math.floor(t / 60), minute: t % 60 }
                            });
                        }}
                    />

                    <div className="time-row">
                        <span>End</span>
                        <strong>{formatTime(form.endTime.hour, form.endTime.minute)}</strong>
                    </div>

                    <input
                        type="range"
                        min="0"
                        max="1439"
                        value={form.endTime.hour * 60 + form.endTime.minute}
                        onChange={(e) => {
                            const t = Number(e.target.value);
                            setForm({
                                ...form,
                                endTime: { hour: Math.floor(t / 60), minute: t % 60 }
                            });
                        }}
                    />

                </div>
            )}

            <button className="btn" onClick={handleSubmit}>
                Save
            </button>

        </div>
    );
};


// ===== MODAL =====
const ModalUI = ({ title, children, onClose }) => {
    return (
        <div className="modal-overlay">
            <div className="modal-box">

                <div className="modal-header">
                    <h3>{title}</h3>
                    <button onClick={onClose}>✕</button>
                </div>

                {children}

            </div>
        </div>
    );
};


export default PlanPage;