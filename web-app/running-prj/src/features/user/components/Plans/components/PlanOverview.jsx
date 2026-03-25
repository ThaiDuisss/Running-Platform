import { Card, Col, Container, Row } from "react-bootstrap";
import { Target, Calendar, Clock, Zap, Activity, ActivitySquare } from "lucide-react";
import { getCache } from "@/shared/services/helperCache";


export const PlanOverview = ({ plan }) => {
    const ONE_WEEK = 24 * 60 * 60 * 1000 * 7;

    const goals = getCache("getAllGoals", ONE_WEEK);
    const goal = goals.filter((a) => a.id === plan.goal);
    return (
        <Card className="p-4 mb-4">
            <div className="d-flex gap-3 align-items-center">
                <div
                    style={{
                        borderRadius: "10px",
                        backgroundColor: "rgba(25, 35, 25, 1)",
                        padding: "8px",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center"
                    }}
                >
                    <Activity size={50} color="white" />
                </div>

                <div className="d-flex flex-column justify-content-center">
                    <h2 className="mb-0">{plan.title}</h2>
                    <p className="mb-0 align-items-center" style={{ color: "rgba(52, 144, 144, 1)" }}>
                        <Target size={16} /> {goal[0]?.description}
                    </p>
                </div>
            </div>

            <div className="d-flex justify-content-between flex-wrap gap-4 mt-5">

                {/* DURATION */}
                <div className="d-flex flex-column">
                    <span className="text-muted small fw-semibold">DURATION</span>
                    <div className="d-flex align-items-center gap-2">
                        <Calendar size={20} />
                        <span className="fw-semibold">{plan.durationWeeks} Weeks</span>
                    </div>
                </div>

                {/* FREQUENCY */}
                <div className="d-flex flex-column">
                    <span className="text-muted small fw-semibold">FREQUENCY</span>
                    <div className="d-flex align-items-center gap-2">
                        <Clock size={20} />
                        <span className="fw-semibold">{plan.daysPerWeek} Days/wk</span>
                    </div>
                </div>

                {/* EXPERIENCE */}
                <div className="d-flex flex-column">
                    <span className="text-muted small fw-semibold">EXPERIENCE</span>
                    <div className="d-flex align-items-center gap-2">
                        <Zap size={20} />
                        <span className="fw-semibold">{plan.selectedLevel}</span>
                    </div>
                </div>

            </div>
        </Card>



    );
};  