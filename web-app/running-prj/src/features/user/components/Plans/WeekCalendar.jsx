import React from "react";
import { Row, Col } from "react-bootstrap";
import DayCard from "./DayCard";
const WeekCalendar = () => {

    return (
        <Row className="g-3">

            <Col>
                <DayCard title="Chạy nhẹ" km="5.0" time="30 phút" />
            </Col>

            <Col>
                <DayCard title="Interval" km="4.5" time="45 phút" />
            </Col>

            <Col>
                <DayCard title="Hôm nay" km="6.0" time="50 phút" active />
            </Col>

            <Col>
                <DayCard title="Chạy nhẹ" km="5.5" time="35 phút" />
            </Col>

            <Col>
                <DayCard title="Dài hạn" km="10.0" time="90 phút" />
            </Col>

        </Row>
    );

};

export default WeekCalendar;