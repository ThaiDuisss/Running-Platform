import React, { useState } from "react";
import { Row, Col, Button } from "react-bootstrap";
import DayCard from "./DayCard";
import "@/style/weekGrid.css"
import StepProgress from "./StepProgress";
import { motion, AnimatePresence } from "framer-motion";

const WeekGrid = ({ schedule = [], onEdit, applyOnWeeks }) => {
    const [currentWeekIndex, setCurrentWeekIndex] = useState(0);
    const [selectedWeeks, setSelectedWeeks] = useState([]);

    const toggleWeek = (weekNumber) => {
        setSelectedWeeks((prev) =>
            prev.includes(weekNumber)
                ? prev.filter((w) => w !== weekNumber)
                : [...prev, weekNumber]
        );
    };
    if (!schedule.length) return null;

    const currentWeek = schedule[currentWeekIndex];

    const goPrev = () => {
        if (currentWeekIndex > 0) {
            setCurrentWeekIndex(currentWeekIndex - 1);
        }
    };

    const goNext = () => {
        if (currentWeekIndex < schedule.length - 1) {
            setCurrentWeekIndex(currentWeekIndex + 1);
        }
    };

    return (
        <div>
            {/* Header */}
            <StepProgress currentStep={currentWeekIndex + 1} totalSteps={schedule.length} text="WEEK" />
            <div className="d-flex justify-content-between align-items-center mb-3">
                <Button variant="outline-secondary" onClick={goPrev} disabled={currentWeekIndex === 0}>
                    ←
                </Button>

                <h5 className="mb-0">
                    Week {currentWeek.week}
                </h5>

                <Button
                    variant="outline-secondary"
                    onClick={goNext}
                    disabled={currentWeekIndex === schedule.length - 1}
                >
                    →
                </Button>
            </div>

            {/* Days */}


            <AnimatePresence mode="wait">
                <motion.div
                    key={currentWeekIndex}
                    initial={{ opacity: 0, x: 50 }}
                    animate={{ opacity: 1, x: 0 }}
                    exit={{ opacity: 0, x: -50 }}
                    transition={{ duration: 0.1 }}>
                    <Row>
                        {currentWeek.days.map((day) => (
                            <Col style={{ width: "170px", height: "100px" }} xs={6} md={3} lg={2} key={day.date} className="mb-5">
                                <DayCard day={day} onClick={() => onEdit(day)} isOpen={true} />
                            </Col>
                        ))}
                    </Row>
                    <h4 className="mt-5">
                        Apply  <span style={{ color: "blue" }}>Week {currentWeekIndex + 1} </span>to Weeks: {selectedWeeks.length != 0 && selectedWeeks.map((e) => { return <span style={{ color: "purple" }}>{e} </span> })}
                    </h4>
                    <div className="week-preview-list">
                        <Row>
                            {schedule.slice(currentWeekIndex + 1).map((week) => (
                                <Col
                                    onClick={() => toggleWeek(week.week)}
                                    key={week.week}
                                    className={`week-preview ${selectedWeeks.includes(week.week) ? "active" : ""}`}
                                    style={{ width: "50px", marginRight: "10px", marginBottom: "10px" }}
                                    lg={2}
                                    xs={4}
                                >
                                    Week {week.week}
                                </Col>
                            ))}
                            <Button
                                className="mt-3"
                                disabled={selectedWeeks.length === 0}
                                onClick={() => {
                                    if (window.confirm("Apply this week to selected weeks?")) {
                                        applyOnWeeks(currentWeekIndex, selectedWeeks);
                                        setSelectedWeeks([]);
                                    }
                                }}
                            >
                                Apply to selected weeks
                            </Button>
                        </Row>
                    </div>

                </motion.div>
            </AnimatePresence>


        </div>
    );
};

export default WeekGrid;