import React from "react";
import { Container, Row, Col } from "react-bootstrap";

import WeekCalendar from "./WeekCalendar";
import RunDetail from "./RunDetail";
import RightPanel from "./RightPanel";
import Sidebar from "./Slidebar";
import Topbar from "./Topbar";
const PlanPage = () => {

    return (

        <Container fluid>

            <Row>

                <Col md={2} className="p-0">
                    <Sidebar />
                </Col>

                <Col md={7} className="p-4">

                    <Topbar />

                    <WeekCalendar />

                    <RunDetail />

                </Col>

                <Col md={3} className="p-4">
                    <RightPanel />
                </Col>

            </Row>

        </Container>

    );

};

export default PlanPage;