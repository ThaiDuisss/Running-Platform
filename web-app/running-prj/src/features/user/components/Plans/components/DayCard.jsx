import React from "react";
import { Card } from "react-bootstrap";
import * as Icons from "lucide-react";
import { TRAINING_TYPES } from "@/shared/constant/constants";

export default function DayCard({ day, onClick, isOpen }) {
    const typeData = TRAINING_TYPES[day.type?.toLowerCase()] || TRAINING_TYPES.rest;
    const isRest = day.type?.toLowerCase() === "rest";
    const Icon = Icons[typeData.icon];

    return (
        <Card
            onClick={(!isRest && isOpen ? onClick : undefined)}
            title={isRest ? "Rest day cannot be edited" : ""}
            style={{
                cursor: isRest ? "not-allowed" : "pointer",
                minHeight: 120,
                borderLeft: `5px solid ${typeData.color}`,
                opacity: isRest ? 0.6 : 1,  
                transition: "0.2s"

            }}
            className="hover-shadow"
        >
            <Card.Body className="d-flex flex-column align-items-center justify-content-center text-center gap-2">

                {/* Day */}
                <small className="text-muted">{day.date}</small>

                {/* Icon */}
                {Icon && <Icon size={20} color={typeData.color} />}

                {/* Label */}
                <h6 style={{ color: typeData.color, margin: 0 }}>
                    {typeData.label}
                </h6>

                {/* Info */}
                {day.type !== "rest" && (
                    <div style={{ fontSize: "0.85rem" }}>
                        {day.targetDistance} km • {day.duration} min
                    </div>
                )}
            </Card.Body>
        </Card>
    );
}