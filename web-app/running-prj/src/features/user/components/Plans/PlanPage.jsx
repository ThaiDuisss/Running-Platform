import React, { useState } from "react";
import { Container, Row, Col } from "react-bootstrap";


import { FaCalendar, FaChevronLeft, FaChevronRight } from "react-icons/fa";
const PlanPage = () => {
    const now = new Date();

    const [date, setDate] = useState(new Date(now.getFullYear(), now.getMonth(), 1));
    const [workout, setWorkout] = useState({
        id: 0,
        title: "",
        distance: "",
        duration: 0,
        isSetTime: false,
        startTime: "",
        endTime: ""
    })
    const [workoutDay, setWorkoutDay] = useState({
        id: "",
        date: null,
        workout: [{}],
    });
    const getFormatYearMonth = (date) => {
        return date.toLocaleDateString("en-US", {
            month: "long",
            year: "numeric"
        });
    }
    const currentDay = now.getDate();
    const getNumberDays = () => {
        const totalDay = new Date(
            date.getFullYear(),
            date.getMonth() + 1,
            0
        ).getDate(); return Array.from({ length: totalDay }, (_, i) => i + 1);
    }
    const prevMonth = () => {
        setDate(new Date(new Date(date.getFullYear(), date.getMonth() - 1, 1)));
    }

    const nextMonth = () => {
        setDate(new Date(new Date(date.getFullYear(), date.getMonth() + 1, 1)));
    }
    return (
        <Container className="py-3">
            <div className="header d-flex justify-content-between mb-4">
                <div className="header-title "><h1 className="text-black">Training Schedule</h1>
                    <div style={{ borderRadius: "999px", border: "1px solid black", width: "fit-content", backgroundColor: "#713030ff" }} className="align-items-center d-flex">
                        <FaChevronLeft color="white" cursor={"pointer"} onClick={() => prevMonth()}></FaChevronLeft>
                        <span className="fw-bold fs-5 mx-4 text-white">{getFormatYearMonth(date)}</span>
                        <FaChevronRight color="white" cursor={"pointer"} onClick={() => nextMonth()}></FaChevronRight>
                    </div>
                </div>
                <FaCalendar size={30} style={{ color: "black" }} />
            </div>
            <div className="button-generate">
            </div>
        </Container>

    );

};

export default PlanPage;