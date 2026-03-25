import React, { useState, useMemo, useEffect } from 'react'
import "@/style/custom_plan_form.css";
import { Button, Card, Col, Container, Form, Row } from 'react-bootstrap';
import {
    Trophy,
    Timer,
    Activity,
    Scale,
    Heart,
    ChevronRight,
    CheckCircle2
} from 'lucide-react';
import DatePicker from "react-datepicker";
import { useRef } from "react";


import { motion, AnimatePresence } from 'motion/react';
import { customPlan } from '@/shared/services/CustomGoal';
import { getCache, setCache } from '@/shared/services/helperCache';
import { usePlanStore } from './usePlanStore';
import { useNavigate } from "react-router-dom";
import StepProgress from './components/StepProgress';
import { Calendar } from 'react-bootstrap-icons';

const CustomPlan = () => {
    const dateRef = useRef();
    const [date, setDate] = useState();
    const form = usePlanStore((state) => state.form);
    const updateForm = usePlanStore(state => state.updateForm);
    const updateParams = usePlanStore(state => state.updateParams);
    const navigate = useNavigate();
    const ICON_MAP = {
        Trophy,
        Timer,
        Activity,
        Scale,
        Heart
    };
    console.log("test", form)
    const ONE_WEEK = 24 * 60 * 60 * 1000 * 7;
    const [goals, setGoals] = useState([]);
    const WEEKDAYS = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];

    const handleParamChange = (name, value) => {
        updateParams(name, value);
    };

    const LEVELS = [
        { id: 'beginner', title: 'Beginner', description: 'New to running' },
        { id: 'intermediate', title: 'Intermediate', description: 'Running 10-20 miles/week' },
        { id: 'advanced', title: 'Advanced', description: 'Running 20-40 miles/week' },
        { id: 'pro', title: 'Pro', description: 'Competitive athlete' },
    ];

    const selectedGoalData = goals.find(g => g.id === form.goal);
    useEffect(() => {
        const fetchGoals = async () => {
            const cached = getCache("getAllGoals", ONE_WEEK);

            if (cached) {
                setGoals(cached);
            } else {
                try {
                    const data = await customPlan.getAllGoal();
                    setGoals(data);
                    setCache("getAllGoals", data);
                } catch (err) {
                    setGoals([{ id: 'distance', title: 'Complete Distance', description: 'Train for a specific race or milestone.', icon: Trophy, params: { "fields": [{ "name": "sessionsPerWeek", "type": "number", "label": "Sessions" }] } },
                    { id: 'time', title: 'Improve Time', description: 'Set a new personal best for a distance.', icon: Timer, params: { "fields": [{ "name": "targetDistance", "type": "number", "label": "Target Distance (km)", "required": true }, { "name": "currentDistance", "type": "number", "label": "Current Distance (km)", "required": true }] } },
                    { id: 'fitness', title: 'Improve Fitness', description: 'Build endurance and overall stamina.', icon: Activity, params: { "fields": [{ "name": "distance", "type": "number", "label": "Distance (km)", "required": true }, { "name": "currentTime", "type": "number", "label": "Current Time (minutes)", "required": true }, { "name": "targetTime", "type": "number", "label": "Target Time (minutes)", "required": true }] } },
                    { id: 'weight', title: 'Weight Loss', description: 'Burn calories and manage your weight.', icon: Scale, params: { "fields": [{ "name": "sessionsPerWeek", "type": "number", "label": "Sessions per week", "required": true }, { "name": "avgDistance", "type": "number", "label": "Average Distance (km)", "required": false }] } },
                    { id: 'health', title: 'Stay Healthy', description: 'Maintain a consistent, healthy lifestyle.', icon: Heart, params: { "fields": [{ "name": "currentWeight", "type": "number", "label": "Current Weight (kg)", "required": true }, { "name": "targetWeight", "type": "number", "label": "Target Weight (kg)", "required": true }] } },]);
                }
            }

        };

        fetchGoals();
    }, []);

    const isFormValid = useMemo(() => {
        if (!form.title || !form.goal || !form.selectedLevel || form.selectedDays.length === 0) return false;
        return true;
    }, [form]);

    const onChange = (e) => {
        const { name, value } = e.target;

        if (name === "durationWeeks") {
            updateForm("durationWeeks", Number(value));

            // xử lý selectedDays
            const newDays = form.selectedDays.slice(0, value);
            updateForm("selectedDays", newDays);
        } else {
            updateForm(name, value);
        }
    };

    const toggleDay = (day) => {
        const currentDays = form.selectedDays;

        if (currentDays.includes(day)) {
            updateForm(
                "selectedDays",
                currentDays.filter(d => d !== day)
            );
            return;
        }

        if (currentDays.length >= form.daysPerWeek) return;

        updateForm("selectedDays", [...currentDays, day]);
    };


    return (
        <Container className='custom-form'>
            <StepProgress currentStep={1} totalSteps={3} />
            <div className='custom-form-header'>
                <h1>Create Your Plan</h1>
                <p>Customize your running schedule to fit your life.</p>
            </div>
            <Form className='form-custom-body'>
                <div className='custom-form-title'>
                    <Form.Label> Plan Title</Form.Label>
                    <Form.Control
                        name="title"
                        value={form.title}
                        onChange={onChange}
                        placeholder='e.g., My First 10K Journey'
                    />                </div>

                <section>
                    <h2 className="section-title d-flex align-items-center gap-2 mb-4">
                        <span className="title-bar"></span>
                        What is your primary goal?
                    </h2>

                    <Row>
                        {goals.length > 0 && goals.map((goal) => {
                            const Icon = ICON_MAP[goal.icon];
                            const isSelected = form.goal === goal.id;
                            return (
                                <Col key={goal.id} xs={12} md={6} lg={4} className="mb-3">
                                    <motion.div
                                        whileHover={{ scale: 1.02 }}
                                        whileTap={{ scale: 0.98 }}
                                    >
                                        <Card
                                            onClick={() => {
                                                updateForm("goal", goal.id);
                                                updateForm("params", {}); // reset params
                                            }}
                                            className={`goal-card ${isSelected ? "selected" : ""}`}
                                        >
                                            <Card.Body>
                                                <div className={`icon-box ${isSelected ? "selected" : ""}`}>
                                                    <Icon size={20} />
                                                </div>

                                                <Card.Title className={isSelected ? "text-orange" : ""}>
                                                    {goal.title}
                                                </Card.Title>

                                                <Card.Text className="text-muted small">
                                                    {goal.description}
                                                </Card.Text>

                                                {isSelected && (
                                                    <motion.div layoutId="activeGoal" className="check-icon">
                                                        <CheckCircle2 size={20} />
                                                    </motion.div>
                                                )}
                                            </Card.Body>
                                        </Card>
                                    </motion.div>
                                </Col>
                            );
                        })}
                    </Row>
                </section>

                <AnimatePresence mode="wait">
                    {selectedGoalData && (
                        <motion.section
                            key={form.goal}
                            initial={{ opacity: 0, height: 0 }}
                            animate={{ opacity: 1, height: "auto" }}
                            exit={{ opacity: 0, height: 0 }}
                            transition={{ duration: 0.3 }}
                            className="bg-white p-4 rounded-3 shadow-sm mt-3"
                        >
                            <h5 className="mb-3">Goal Details</h5>

                            <Row>
                                {selectedGoalData.params != null ? selectedGoalData.params?.fields?.map((field) => (
                                    <Col md={6} key={field.name}>
                                        <Form.Group className="mb-3">
                                            <Form.Label>{field.label}</Form.Label>

                                            <Form.Control
                                                type={field.type}
                                                value={form.params?.[field.name] || ""}
                                                onChange={(e) =>
                                                    handleParamChange(field.name, e.target.value)
                                                }
                                                required={field.required}
                                            />
                                        </Form.Group>
                                    </Col>
                                )) : <p style={{ color: "rgba(25, 121, 121, 1)" }}>We'll focus your plan on consistent aerobic base building and recovery.</p>}
                            </Row>
                        </motion.section>
                    )}
                </AnimatePresence>


                <section className="training-section">
                    <h2 className="section-title">Training Setup</h2>

                    <Row className="mb-4">
                        {/* Duration */}
                        <Col md={6}>
                            <div className="form-group-custom">
                                <label className="label-custom">
                                    Duration {form.durationWeeks}(Weeks)
                                </label>

                                <Form.Range name='durationWeeks' min={4} max={19} step={1} value={form.durationWeeks} onChange={(e) => onChange(e)} />
                            </div>
                        </Col>

                        {/* Days per week */}
                        <Col md={6}>
                            <div className="form-group-custom">
                                <Form.Group>
                                    <Form.Label className="label-custom">
                                        Days Per Week
                                    </Form.Label>

                                    <div className="d-flex gap-2">
                                        {[1, 2, 3, 4, 5, 6, 7].map(num => (
                                            <Form.Check
                                                key={num}
                                                type="radio"
                                                name="daysPerWeek"
                                                id={`day-${num}`}
                                                label={num}
                                                value={num}
                                                checked={form.daysPerWeek === num}
                                                onChange={(e) => {
                                                    const value = Number(e.target.value);

                                                    updateForm("daysPerWeek", value);

                                                    const newDays = form.selectedDays.slice(0, value);
                                                    updateForm("selectedDays", newDays);
                                                }}
                                                className="day-radio"
                                            />
                                        ))}
                                    </div>

                                </Form.Group>
                            </div>
                            <Form.Group>
                                <Form.Label>Select day to start:</Form.Label>

                                <div className="position-relative">
                                    <DatePicker
                                        ref={dateRef}
                                        selected={form.dayStart}
                                        onChange={(date) => updateForm("dayStart", date)}
                                        minDate={new Date()}
                                        className="form-control pe-5"
                                        placeholderText="Select a date"
                                        dateFormat="dd/MM/yyyy"
                                    />

                                    {/* Icon */}
                                    <Calendar
                                        size={18}
                                        className="position-absolute top-50 end-0 translate-middle-y me-5 text-muted"
                                        style={{ cursor: "pointer" }}
                                        onClick={() => dateRef.current.setFocus()}
                                    />
                                </div>
                            </Form.Group>
                        </Col>
                    </Row>

                    {/* Preferred days */}
                    <div className="form-group-custom">
                        <label className="label-custom">
                            Preferred Training Days
                        </label>

                        <div className="d-flex flex-wrap gap-2">
                            {WEEKDAYS.map(day => {
                                const isSelected = form.selectedDays.includes(day);

                                return (
                                    <Button
                                        key={day}
                                        onClick={() => toggleDay(day)}
                                        className={`weekday-btn ${isSelected ? "active" : ""}`}
                                    >
                                        {day}
                                    </Button>
                                );
                            })}
                        </div>

                        <p className="tip-text">
                            Tip: Try to spread your runs evenly throughout the week for better recovery.
                        </p>
                    </div>
                </section>

                <section className="experience-section">
                    <h2 className="section-title">Experience Level</h2>

                    <Row>
                        {LEVELS.map((level) => {
                            const isSelected = form.selectedLevel === level.id;
                            return (
                                <Col xs={6} sm={3} key={level.id} className="mb-3">
                                    <Form.Check
                                        type="radio"
                                        name="level"
                                        id={`level-${level.id}`}
                                        value={level.id}
                                        checked={isSelected}
                                        onChange={(e) =>
                                            updateForm("selectedLevel", e.target.value)
                                        }
                                        label={
                                            <div className="level-content">
                                                <h3 className={`level-title ${isSelected ? "active" : ""}`}>
                                                    {level.title}
                                                </h3>
                                                <p className="level-desc">
                                                    {level.description}
                                                </p>
                                            </div>
                                        }
                                        className="level-radio"
                                    />
                                </Col>
                            );
                        })}
                    </Row>
                </section>

                <section className="next-step-section">
                    <motion.div
                        whileHover={isFormValid ? { scale: 1.01 } : {}}
                        whileTap={isFormValid ? { scale: 0.99 } : {}}
                    >
                        <Button
                            disabled={!isFormValid}
                            className={`next-btn ${isFormValid ? "active" : "disabled"}`}
                            onClick={() => navigate("/plans/custom-plan/schedule")}
                        >
                            Next Step
                            <ChevronRight size={20} />
                        </Button>
                    </motion.div>

                    {!isFormValid && (
                        <p className="next-tip">
                            Please fill in all required fields to continue.
                        </p>
                    )}
                </section>
            </Form>

        </Container>
    )
}

export default CustomPlan
