import React from "react";
import { Card, Badge, Button } from "react-bootstrap";
import "@/style/plan.css";
const DayCard = ({ title, km, time, active }) => {

    return (
        <Card className={`day-card p-3 ${active ? "active" : ""}`}>

            <Badge bg={active ? "primary" : "secondary"}>
                {title}
            </Badge>

            <h4 className="mt-2">
                {km} km
            </h4>

            <small className="text-muted">
                {time}
            </small>

            {active &&
                <Button className="mt-2">
                    Bắt đầu
                </Button>
            }

        </Card>
    );

};

export default DayCard;