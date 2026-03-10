import React from "react";
import { Card, ProgressBar } from "react-bootstrap";
import "@/style/plan.css";
const RightPanel = () => {

    return (
        <Card className="right-panel p-3">

            <h5>
                Tiến độ tuần
            </h5>

            <ProgressBar now={62} label="62%" />

        </Card>
    );

};

export default RightPanel;