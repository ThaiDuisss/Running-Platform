import { Ticket } from "lucide-react";
import React from "react";
import { Card, Button } from "react-bootstrap";
import { CheckCircle } from "react-bootstrap-icons";
import { FcLeft } from "react-icons/fc";
import { Link } from "react-router-dom";

export default function SummaryPanel({ stats, prefs, onNavigate }) {
    return (
        <Card
            style={{
                maxWidth: "350px",
                backgroundColor: "rgba(19, 39, 60, 1)",
                height: "500px",
                boxShadow: "0 10px 30px rgba(0,0,0,0.3)" // 👈 thêm dòng này
            }}
        >            <Card.Body className="d-flex  flex-column gap-4">
                <h4 style={{ color: "hsla(213, 44%, 95%, 0.92)" }} >Session Summary</h4>

                <p style={{ color: "hsla(213, 44%, 95%, 0.92)" }}>Total Distance: {stats.totalDistance} km</p>
                <p style={{ color: "hsla(213, 44%, 95%, 0.92)" }}>Total Time: {stats.totalDuration} min</p>
                <p style={{ color: "hsla(213, 44%, 95%, 0.92)" }}>Sessions: {stats.sessionCount}</p>
                <div style={{ display: "flex", flexDirection: "column", gap: 2 }}>
                    <div style={{ opacity: 0.6, margin: "20px auto", width: "90%", height: "1px", backgroundColor: " #5fa3e8ff" }}></div>
                    <div style={{ display: "flex", justifyContent: "space-between" }}>
                        <p style={{ color: "hsla(214, 9%, 66%, 0.92)" }}> Plan status</p>
                        <p style={{ color: "rgba(25, 198, 111, 1)" }}><CheckCircle />Ready to go</p>
                    </div>
                    <Button
                        variant="dangerous"
                        className="w-100 d-flex align-items-center justify-content-center"
                        style={{ height: "50px", marginBottom: "20px", color: "white" }}
                        onClick={() => onNavigate()}
                    >
                        Next Step
                    </Button>
                    <Link style={{ cursor: "pointer" }} to={"/plans/custom-plan"}><FcLeft />Back to Goals</Link>
                </div>

            </Card.Body>
        </Card >
    );
}