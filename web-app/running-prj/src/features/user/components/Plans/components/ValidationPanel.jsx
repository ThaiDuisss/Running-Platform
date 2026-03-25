import React from "react";
import { Card } from "react-bootstrap";
import ValidationItem from "./ValidationItem";

export default function ValidationPanel({ stats, prefs }) {
    return (
        <Card className="mt-3">
            <Card.Body>
                <h6>Validation</h6>

                <ValidationItem
                    label="Training Days"
                    current={stats.sessionCount}
                    target={prefs.daysPerWeek}
                />

                <ValidationItem
                    label="Rest Days"
                    current={stats.restDaysCount}
                    target="1+"
                />
            </Card.Body>
        </Card>
    );
}