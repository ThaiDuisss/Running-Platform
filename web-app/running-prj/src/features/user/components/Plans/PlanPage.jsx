import React, { useState, useEffect, useRef, useLayoutEffect } from "react";
import { Container, Row, Col, Form } from "react-bootstrap";
import { FaChevronLeft, FaChevronRight, FaMagic, FaPlus } from "react-icons/fa";
import { planService } from "@/app/services/PlanService";
import InstanceWorkoutDay from "./InstanceWorkoutDay";
import "@/style/plan.css";
import { Plus, PlusCircle } from "react-bootstrap-icons";
import { useNavigate } from "react-router-dom";
import { useLocation } from "react-router-dom";


const PlanPage = () => {
    const now = new Date();

    const [date, setDate] = useState(new Date(now.getFullYear(), now.getMonth(), 1));
    const [plans, setPlans] = useState([]);
    const [loading, setLoading] = useState(false);
    const location = useLocation();
    const [showCRUD, setShowCRUD] = useState(false);
    const [selectedDate, setSelectedDate] = useState(null);
    const [selectedPlan, setSelectedPlan] = useState(null);

    const [showAI, setShowAI] = useState(false);
    const [showCustom, setShowCustom] = useState(false);
    const todayRef = useRef(null);
    const hasScrolled = useRef(false);
    const [message, setMessage] = useState("");

    const navigate = useNavigate();

    useLayoutEffect(() => {
        if (todayRef.current && !hasScrolled.current) {
            todayRef.current.scrollIntoView({
                behavior: "auto",
                block: "start"
            })
            hasScrolled.current = true;
        }
    }, [plans]);

    useEffect(() => {
        if (location.state?.message) {
            setMessage(location.state.message);

            // clear state
            navigate(location.pathname, { replace: true });

            // ⏱️ auto hide sau 3s
            setTimeout(() => {
                setMessage("");
            }, 3000);
        }
    }, [location.state]);

    useEffect(() => {
        fetchPlans();
        hasScrolled.current = false;
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
    const compareDate = (dayInstance) => {
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        const compareDay = new Date(dayInstance);
        compareDay.setHours(0, 0, 0, 0);

        return compareDay < today;
    };

    const isMonthNow = (dateSelect) => {
        return now.getFullYear() === dateSelect.getFullYear() && now.getMonth() === dateSelect.getMonth();
    };

    const isDayNow = (dateSelect) => {
        return now.getFullYear() === dateSelect.getFullYear() && now.getMonth() === dateSelect.getMonth() && dateSelect.getDate() === now.getDate();
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
            {message && (
                <div className="custom-alert">
                    {message}
                </div>
            )}


            {/* AI + CUSTOM */}
            <Row className="mb-4 g-3">
                <Col md={6}>
                    <div className="plan-card ai" onClick={() => navigate("/plans/ai-generate")}>
                        <FaMagic size={22} />
                        <h4>AI Generate Plan</h4>
                        <p>Let AI build your schedule</p>
                    </div>
                </Col>

                <Col md={6}>
                    <div className="plan-card custom" onClick={() => navigate("/plans/custom-plan")}>
                        <FaPlus size={22} />
                        <h4>Custom Plan</h4>
                        <p>Create your own workouts</p>
                    </div>
                </Col>
            </Row>
            {/* HEADER */}
            <div className="plan-header">
                <h2>Training Schedule</h2>

                <div className="month-nav">
                    <FaChevronLeft onClick={() =>
                        setDate(new Date(date.getFullYear(), date.getMonth() - 1, 1))
                    } />
                    <span style={{ fontSize: 20 }}>
                        {date.toLocaleDateString("en-US", { month: "long", year: "numeric" })}
                    </span>
                    <FaChevronRight onClick={() =>
                        setDate(new Date(date.getFullYear(), date.getMonth() + 1, 1))
                    } />
                </div>
            </div>
            {/* LIST */}
            <div className="calendar-list" >
                {Array.from({ length: getDaysInMonth() }, (_, i) => i + 1).map(day => {
                    const dayPlans = getPlansForDate(day);
                    const dayDate = new Date(date.getFullYear(), date.getMonth(), day);
                    const isDate = isDayNow(dayDate);
                    return (
                        <div key={day} className="calendar-row" ref={isDate ? todayRef : null} >

                            <div className={isDate ? "calendar-date-now" : "calendar-date"}>
                                <span className="day-name">
                                    {dayDate.toLocaleDateString("en-US", { weekday: "short" })}
                                </span>
                                <span className="day-number">{day}</span>
                            </div>
                            {
                                dayPlans.length > 0 ? (
                                    <>
                                        <div className="day-workouts-scroll" >
                                            {dayPlans.map(plan => (
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
                                            ))}
                                        </div>

                                        <div
                                            className={`add-sticky-btn ${!compareDate(dayDate) ? "" : "disabled"
                                                }`}
                                            onClick={() => {
                                                if (!compareDate(dayDate)) {
                                                    setSelectedPlan(null);
                                                    setSelectedDate(dayDate);
                                                    setShowCRUD(true);
                                                }
                                            }}
                                        >
                                            <PlusCircle size={40} />
                                        </div>
                                    </>

                                ) : (
                                    <div
                                        className={!compareDate(dayDate) ? "empty-workout" : "empty-workout-strict"}
                                        onClick={() => {
                                            if (!compareDate(dayDate)) {
                                                setSelectedPlan(null);
                                                setSelectedDate(dayDate);
                                                setShowCRUD(true);
                                            }
                                        }}
                                    >
                                        + Add workout
                                    </div>
                                )
                            }
                        </div>

                    );

                })}
            </div>


            {/* CRUD MODAL */}
            {
                showCRUD && (
                    <ModalUI title="Workout Detail" dateTime={selectedDate} onClose={() => setShowCRUD(false)}>
                        <FormUI onSubmit={handleSubmit} data={selectedPlan} />
                    </ModalUI>
                )
            }
            {/* AI MODAL */}
            {
                showAI && (
                    <ModalUI title="AI Generate Plan" onClose={() => setShowAI(false)}>
                        <input className="input" placeholder="Goal..." />
                        <input className="input" placeholder="Level..." />
                        <button className="btn">Generate</button>
                    </ModalUI>
                )
            }

            {/* CUSTOM MODAL */}
            {
                showCustom && (
                    <ModalUI title="Custom Plan" onClose={() => setShowCustom(false)}>
                        <input className="input" placeholder="Plan name" />
                        <input className="input" type="date" />
                        <button className="btn">Create</button>
                    </ModalUI>
                )
            }
            {
                !isMonthNow(date) && <div className="d-flex justify-content-center">
                    <button
                        className="back-to-today-btn"
                        onClick={() => {
                            const today = new Date();

                            setDate(new Date(today.getFullYear(), today.getMonth(), 1));

                            hasScrolled.current = false;
                        }}
                    >
                        Back to current day
                    </button>
                </div>
            }


        </Container >
    );
};

// ===== FORM =====
const FormUI = ({ onSubmit, data }) => {

    const parseTime = (timeStr) => {
        if (!timeStr) return { hour: 6, minute: 0 };

        if (typeof timeStr === "object") return timeStr; // tránh crash

        const [h, m] = timeStr.split(":").map(Number);
        return { hour: h, minute: m };
    };
    const [form, setForm] = useState({
        title: data?.title || "",
        targetDistance: data?.targetDistance || "",
        duration: data?.duration || "",
        isSetTime: data?.isSetTime || false,
        startTime: parseTime(data?.startTime),
        endTime: parseTime(data?.endTime)
    });

    const formatTime = (h, m) => {
        const ampm = h >= 12 ? "PM" : "AM";
        const h12 = h % 12 || 12;
        return `${h12}:${String(m).padStart(2, "0")} ${ampm}`;
    };

    const toTimeString = (t) => {
        return `${String(t.hour).padStart(2, "0")}:${String(t.minute).padStart(2, "0")}:00`;
    };



    const handleSubmit = (e) => {
        e.preventDefault();
        if (!form.duration || form.duration <= 0) {
            alert("Please enter duration before submitting!");
            return;
        }
        onSubmit({
            title: form.title,
            targetDistance: form.targetDistance,
            duration: form.duration,
            isSetTime: form.isSetTime,

            startTime: form.isSetTime ? toTimeString(form.startTime) : null,
            endTime: form.isSetTime ? toTimeString(form.endTime) : null,

            completed: false
        });
    };
    const normalize = (m) => (m + 1440) % 1440;

    const toTimeObj = (minutes) => ({
        hour: Math.floor(minutes / 60),
        minute: minutes % 60
    });

    const onchange = (e) => {
        const { name, value, checked } = e.target;

        if (name === "isSetTime") {
            setForm((prev) => ({ ...prev, isSetTime: checked }));
            return;
        }

        if (name === "startTime" || name === "endTime") {
            const time = Number(value);

            setForm((prev) => {
                const duration = Number(prev.duration) || 0;

                let startMinutes =
                    prev.startTime.hour * 60 + prev.startTime.minute;

                let endMinutes =
                    prev.endTime.hour * 60 + prev.endTime.minute;

                if (name === "startTime") {
                    startMinutes = time;
                    endMinutes = normalize(startMinutes + duration);
                } else {
                    endMinutes = time;
                    startMinutes = normalize(endMinutes - duration);
                }

                return {
                    ...prev,
                    startTime: toTimeObj(startMinutes),
                    endTime: toTimeObj(endMinutes)
                };
            });

            return;
        }

        // input thường
        setForm((prev) => ({
            ...prev,
            [name]:
                name === "targetDistance" || name === "duration"
                    ? Number(value)
                    : value
        }));
    };

    return (
        <div className="activity-form">
            <div className="activity-card">
                <h3>{form.title || "Workout"}</h3>

                <div className="activity-meta text-black">
                    {form.targetDistance} KM • {form.duration} min
                </div>
            </div>
            <Form onSubmit={handleSubmit}>
                <Form.Control className="input" placeholder="Workout title" value={form.title} onChange={(e) => onchange(e)} required name="title" />
                <Form.Control className="input" placeholder="Distance" type="number" value={form.targetDistance} onChange={(e) => onchange(e)} required name="targetDistance" />
                <Form.Control className="input" placeholder="Duration" type="number" value={form.duration} onChange={(e) => onchange(e)} required name="duration" />
                <Form.Group className="my-4">
                    <div className="d-flex align-items-center justify-content-between">
                        <span className="text-white fw-4">Schedule Time</span>
                        <Form.Check
                            name="isSetTime"
                            checked={form.isSetTime}
                            onChange={onchange}
                        />
                    </div>
                </Form.Group>
                <div className="time-box">
                    <Form.Group>
                        <Form.Label className="time-row">
                            <span>Start time</span>
                            <strong>{formatTime(form.startTime.hour, form.startTime.minute)}</strong>
                        </Form.Label>
                        <Form.Range disabled={!form.isSetTime} name="startTime" value={form.startTime.hour * 60 + form.startTime.minute}
                            min={0} max={1439}
                            onChange={(e) => onchange(e)}></Form.Range>
                    </Form.Group>
                    <Form.Group>
                        <Form.Label className="time-row">
                            <span>End time</span>
                            <strong>{formatTime(form.endTime.hour, form.endTime.minute)}</strong>
                        </Form.Label>
                        <Form.Range disabled={!form.isSetTime} name="endTime" value={form.endTime.hour * 60 + form.endTime.minute} min={0} max={1439} onChange={(e) => onchange(e)}></Form.Range>
                    </Form.Group>
                </div>
                <div className="d-flex justify-content-end">
                    <button
                        type="submit"
                        className={!form.duration || form.duration <= 0 || form.title.length === 0 ? "btn-disable" : "btn"}
                        disabled={!form.duration || form.duration <= 0}
                    >
                        Save
                    </button>
                </div>

            </Form >

        </div >
    );
};


// ===== MODAL =====
const ModalUI = ({ title, children, onClose, dateTime }) => {
    return (
        <div className="modal-overlay">

            <div className="modal-box">
                <div className="modal-header">
                    <h3>{title}</h3>
                    <div className="calendar-date">
                        <div className="day-header">
                            {dateTime.toLocaleDateString("en-US", {
                                weekday: "short",
                                year: "numeric",
                                month: "short",
                                day: "numeric"
                            })}
                        </div>
                    </div>
                    <button onClick={onClose}>✕</button>
                </div>

                {children}

            </div>
        </div>
    );
};


export default PlanPage;