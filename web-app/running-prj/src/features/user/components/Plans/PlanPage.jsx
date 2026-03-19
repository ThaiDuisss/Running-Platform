import React, { useState, useEffect } from "react";
import { Container } from "react-bootstrap";
import { FaCalendar, FaChevronLeft, FaChevronRight } from "react-icons/fa";
import axios from "axios";
import InstanceWorkoutDay from "./InstanceWorkoutDay";

const PlanPage = () => {
    const now = new Date();

    const [date, setDate] = useState(
        new Date(now.getFullYear(), now.getMonth(), 1)
    );

    const [workoutDays, setWorkoutDays] = useState([]);
    const [loading, setLoading] = useState(false);

    // =============================
    // 📌 FORMAT HEADER
    // =============================
    const getFormatYearMonth = (date) => {
        return date.toLocaleDateString("en-US", {
            month: "long",
            year: "numeric",
        });
    };

    // =============================
    // 📌 GET ALL DAYS IN MONTH
    // =============================
    const getNumberDays = () => {
        const totalDay = new Date(
            date.getFullYear(),
            date.getMonth() + 1,
            0
        ).getDate();

        return Array.from({ length: totalDay }, (_, i) => i + 1);
    };

    // =============================
    // 📌 CALL BE
    // =============================
    const fetchPlans = async () => {
        setLoading(true);
        try {
            const res = await axios.get("http://localhost:8080/plans", {
                params: {
                    year: date.getFullYear(),
                    month: date.getMonth() + 1,
                },
            });

            const plans = res.data.result || [];

            // GROUP BY DATE
            const map = {};

            plans.forEach((p) => {
                const key = p.scheduledDate;

                if (!map[key]) {
                    map[key] = [];
                }

                map[key].push(p);
            });

            // BUILD FULL MONTH
            const full = getNumberDays().map((day) => {
                const d = new Date(
                    date.getFullYear(),
                    date.getMonth(),
                    day
                );

                const key = d.toISOString().split("T")[0];

                return {
                    id: key,
                    date: d,
                    workouts: map[key] || [],
                };
            });

            setWorkoutDays(full);
        } catch (err) {
            console.error("Fetch error:", err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchPlans();
    }, [date]);

    // =============================
    // 📌 CRUD
    // =============================
    const handleAddWorkout = async (dateId) => {
        try {
            await axios.post("http://localhost:8080/plans", {
                scheduledDate: dateId,
                targetDistance: 5,
                title: "New Workout",
                duration: 30,
                isSetTime: false,
                isCompleted: false,
            });

            fetchPlans();
        } catch (err) {
            console.error(err);
        }
    };

    const handleDeleteWorkout = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/plans/${id}`);
            fetchPlans();
        } catch (err) {
            console.error(err);
        }
    };

    // =============================
    // 📌 CHANGE MONTH
    // =============================
    const prevMonth = () => {
        setDate(
            new Date(date.getFullYear(), date.getMonth() - 1, 1)
        );
    };

    const nextMonth = () => {
        setDate(
            new Date(date.getFullYear(), date.getMonth() + 1, 1)
        );
    };

    return (
        <Container className="py-3">
            {/* HEADER */}
            <div className="header d-flex justify-content-between mb-4">
                <div className="header-title">
                    <h1 className="text-black">Training Schedule</h1>

                    <div
                        style={{
                            borderRadius: "999px",
                            border: "1px solid black",
                            width: "fit-content",
                            backgroundColor: "#713030ff",
                            padding: "6px 12px",
                        }}
                        className="align-items-center d-flex"
                    >
                        <FaChevronLeft
                            color="white"
                            style={{ cursor: "pointer" }}
                            onClick={prevMonth}
                        />

                        <span className="fw-bold fs-5 mx-4 text-white">
                            {getFormatYearMonth(date)}
                        </span>

                        <FaChevronRight
                            color="white"
                            style={{ cursor: "pointer" }}
                            onClick={nextMonth}
                        />
                    </div>
                </div>

                <FaCalendar size={30} style={{ color: "black" }} />
            </div>

            {/* LIST */}
            {loading ? (
                <div>Loading...</div>
            ) : (
                workoutDays.map((day) => (
                    <InstanceWorkoutDay
                        key={day.id}
                        date={day.date}
                        workouts={day.workouts}
                        onAdd={handleAddWorkout}
                        onDelete={handleDeleteWorkout}
                    />
                ))
            )}
        </Container>
    );
};

export default PlanPage;