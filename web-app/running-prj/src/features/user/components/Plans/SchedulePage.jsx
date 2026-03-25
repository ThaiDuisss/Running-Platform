import React, { useEffect, useMemo, useState } from 'react'
import StepProgress from './components/StepProgress'
import { Col, Container, Row } from 'react-bootstrap'
import "@/style/custom_schedule.css";
import { usePlanStore } from './usePlanStore';
import ValidationPanel from './components/ValidationPanel';
import SummaryPanel from './components/SummaryPanel';
import EditModal from './components/EditModal';
import WeekGrid from './components/WeekGrid';
import { Coffee, Flame, Footprints, Timer, Zap } from 'lucide-react';
import { DAYS } from '@/shared/constant/constants';
import { useNavigate } from 'react-router-dom';

const SchedulePage = () => {
    const form = usePlanStore((state) => state.form);
    const schedule = form.schedule || [];
    const generateSchedule = usePlanStore((s) => s.generateSchedule);
    const updateDay = usePlanStore((s) => s.updateDay);
    const applyOnWeeks = usePlanStore((s) => s.applyWeekTemplate);
    const navigate = useNavigate();
    const [editingDay, setEditingDay] = useState(null);
    useEffect(() => {
        if (
            form.selectedDays.length > 0 &&
            form.durationWeeks > 0 &&
            form.schedule.length === 0

        ) {
            generateSchedule();
        }
    }, [form]);
    const stats = useMemo(() => {

        const allDays = schedule.flatMap(w => w.days); // 🔥 quan trọng

        const trainingDays = allDays.filter(d => d.type !== "REST");

        return {
            totalDistance: trainingDays.reduce((a, d) => a + (d.targetDistance || 0), 0),
            totalDuration: trainingDays.reduce((a, d) => a + (d.duration || 0), 0),
            sessionCount: trainingDays.length,
            restDaysCount: allDays.filter(d => d.type === "REST").length,
        };
    }, [schedule]);

    const updateDayHandler = (day) => {
        updateDay(day); // zustand
        setEditingDay(null);
    };
    const applyWeekTemplate = (sourceWeekIndex, targetWeeks) => {
        console.log("hello")
        applyOnWeeks(sourceWeekIndex, targetWeeks);
    }

    return (
        <Container fluid className="p-5" style={{ display: "flex", gap: 20, flexDirection: "column", backgroundColor: "rgba(247, 249, 251, 1)" }}>
            <StepProgress currentStep={2} totalSteps={3} />
            <div className='custom-form-header'>
                <h1>Customize Your Weekly Plan</h1>
                <p>Fine-tune each day of your training schedule. We've pre-filled a plan based on your goals.</p>
            </div>

            <div>

            </div>
            <Row>
                <Col lg={9}>
                    <div style={{ marginBottom: 100 }} >
                        <WeekGrid schedule={schedule} onEdit={setEditingDay} applyOnWeeks={applyWeekTemplate} />

                    </div>

                    <ValidationPanel stats={stats} prefs={form} />
                </Col>

                <Col lg={3}>
                    <SummaryPanel stats={stats} prefs={form} onNavigate={() => navigate("/plans/custom-plan/review")} />
                </Col>
            </Row>

            <EditModal
                show={!!editingDay}
                day={editingDay}
                onClose={() => setEditingDay(null)}
                onSave={updateDayHandler}
            />
        </Container>
    );
}

export default SchedulePage
