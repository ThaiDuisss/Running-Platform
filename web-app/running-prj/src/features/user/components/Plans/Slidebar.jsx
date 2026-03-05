import React from "react";
import { Nav } from "react-bootstrap";
import { Home, Calendar, BarChart, Users, Settings } from "lucide-react";
import "@/style/plan.css";
const Sidebar = () => {
    return (
        <div className="sidebar bg-light p-3">

            <h5 className="fw-bold mb-4">
                RUNNER PLAN
            </h5>

            <Nav className="flex-column gap-2">

                <Nav.Link>
                    <Home size={18} className="me-2" />
                    Trang Chủ
                </Nav.Link>

                <Nav.Link className="active">
                    <Calendar size={18} className="me-2" />
                    Lịch Trình
                </Nav.Link>

                <Nav.Link>
                    <BarChart size={18} className="me-2" />
                    Thống Kê
                </Nav.Link>

                <Nav.Link>
                    <Users size={18} className="me-2" />
                    Cộng Đồng
                </Nav.Link>

                <Nav.Link>
                    <Settings size={18} className="me-2" />
                    Cài Đặt
                </Nav.Link>

            </Nav>

        </div>
    );
};

export default Sidebar;