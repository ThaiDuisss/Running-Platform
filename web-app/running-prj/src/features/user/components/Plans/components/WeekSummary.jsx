import { Activity } from 'lucide-react'
import React from 'react'
import { Card } from 'react-bootstrap'

const WeekSummary = ({ totalDistance, totalDuration, sessionCount }) => {

    // format phút -> giờ + phút
    const formatTime = (minutes) => {
        const h = Math.floor(minutes / 60)
        const m = minutes % 60
        return `${h}h ${m}m`
    }

    const Item = ({ value, label }) => (
        <div
            style={{
                flex: 1,
                background: "#f1f3f6",
                borderRadius: "16px",
                padding: "20px",
                textAlign: "center"
            }}
        >
            <div style={{
                fontSize: "32px",
                fontWeight: "700",
                color: "#111827"
            }}>
                {value}
            </div>

            <div style={{
                marginTop: "5px",
                fontSize: "12px",
                letterSpacing: "1px",
                color: "#6b7280",
                fontWeight: "600"
            }}>
                {label}
            </div>
        </div>
    )

    return (
        <Card
            style={{
                backgroundColor: "#f7f8fa",
                borderRadius: "20px",
                padding: "20px",
                border: "none",
                boxShadow: "0 10px 25px rgba(0,0,0,0.08)"
            }}
        >
            <Card.Body>

                {/* Header */}
                <div style={{
                    display: "flex",
                    alignItems: "center",
                    gap: "10px",
                    marginBottom: "20px"
                }}>
                    <Activity size={28} color="#6366f1" />
                    <h4 style={{
                        margin: 0,
                        fontWeight: "700",
                        color: "#111827"
                    }}>
                        Weekly Summary
                    </h4>
                </div>

                {/* Content */}
                <div style={{
                    display: "flex",
                    gap: "20px"
                }}>
                    <Item
                        value={sessionCount}
                        label="SESSIONS"
                    />

                    <Item
                        value={totalDistance?.toFixed(1)}
                        label="TOTAL KM"
                    />

                    <Item
                        value={formatTime(totalDuration)}
                        label="EST. TIME"
                    />
                </div>

            </Card.Body>
        </Card>
    )
}

export default WeekSummary