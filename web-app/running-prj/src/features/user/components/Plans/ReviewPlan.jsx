import React, { useMemo, useState } from 'react'
import StepProgress from './components/StepProgress'
import "@/style/reviewPlan.css";
import { Button, Card, Col, Container, Row } from 'react-bootstrap';
import { usePlanStore } from './usePlanStore';
import { PlanActions } from './components/PlanActions';
import { PlanOverview } from './components/PlanOverview';
import DayCard from './components/DayCard';
import { AnimatePresence, motion } from 'framer-motion';
import WeekSummary from './components/WeekSummary';
import { customPlan } from '@/shared/services/CustomGoal';
import { useNavigate } from 'react-router-dom';

const ReviewPlan = () => {
    const form = usePlanStore((s) => s.form);
    const [currentWeekIndex, setCurrentWeekIndex] = useState(0);
    const schedule = form.schedule;
    const currentWeek = schedule[currentWeekIndex];
    const navigate = useNavigate();
    const getTrainingDaysPayload = usePlanStore((s) => s.getTrainingDaysPayload);

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
    const statsFunc = () => {
        // const allDays = schedule[currentWeekIndex].days.flatMap(w => w.days); // 🔥 quan trọng

        const trainingDays = schedule[currentWeekIndex].days.filter(d => d.type !== "REST");
        console.log("alo", trainingDays);
        return {
            totalDistance: trainingDays.reduce((a, d) => a + (d.targetDistance || 0), 0),
            totalDuration: trainingDays.reduce((a, d) => a + (d.duration || 0), 0),
            sessionCount: trainingDays.length,
            // restDaysCount: allDays.filter(d => d.type === "REST").length,
        };
    };
    const sats = statsFunc();
    const savePlan = async () => {
        try {
            const payload = {
                ...form,
                schedule: getTrainingDaysPayload()
            };
            console.log("ghelođ", getTrainingDaysPayload(), payload);
            const dataRequest = await customPlan.savePlan(payload);
            localStorage.removeItem("plan-storage");
            navigate("/plans", {
                state: { message: "Plan created successfully 🎉" }
            });
        } catch (error) {
            console.error(error);
        }
    }
    return (
        <Container fluid className='custom-form'>
            <StepProgress currentStep={3} totalSteps={3} />
            <div className='custom-form-header'>
                <h1>Review Your Plan</h1>
                <p>Make sure everything looks good before finalizing.</p>
            </div>
            <PlanOverview plan={form} />
            <Card style={{ padding: "50px" }}>
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
                <div className='ms-3'>
                    <AnimatePresence mode='wait'>
                        <motion.div key={currentWeekIndex}
                            initial={{ opacity: 0, x: 50 }}
                            animate={{ opacity: 1, x: 0 }}
                            exit={{ opacity: 0, x: -50 }}>
                            <Row>
                                {currentWeek.days.map((day) => (
                                    <Col style={{ width: "170px", height: "100px" }} xs={3} md={3} lg={2} key={day.date} className="mb-5">
                                        <DayCard day={day} onClick={() => onEdit(day)} isOpen={false} />
                                    </Col>
                                ))}
                            </Row>
                        </motion.div>
                    </AnimatePresence>

                </div>

            </Card>
            <WeekSummary totalDistance={sats.totalDistance} totalDuration={sats.totalDuration} sessionCount={sats.sessionCount} />
            <PlanActions onBack={"/plans/custom-plan/schedule"} onSave={savePlan} />

        </Container>
    )
}

export default ReviewPlan
