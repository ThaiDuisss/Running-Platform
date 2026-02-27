import React from "react";
import { Card, ProgressBar } from "react-bootstrap";
import "@/style/plan.css";
const RunDetail = () => {

    return (
        <Card className="run-detail p-3 mt-3">

            <h5>
                Chi tiết buổi chạy
            </h5>

            <p>
                Quãng đường: <b>6 km</b>
            </p>

            <ProgressBar>

                <ProgressBar now={30} />

                <ProgressBar variant="danger" now={40} />

                <ProgressBar variant="success" now={30} />

            </ProgressBar>

        </Card>
    );

};

export default RunDetail;