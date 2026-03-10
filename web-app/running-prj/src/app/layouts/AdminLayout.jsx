import { Container, Row, Col } from "react-bootstrap";
import { useState } from "react";
import AdminHeader from "@/features/admin/layout/AdminHeader";
import AdminSidebar from "@/features/admin/layout/AdminSidebar";
import { Outlet } from "react-router-dom";

const AdminLayout = () => {
    const [collapsed, setCollapsed] = useState(false);

    return (
        <Container fluid className="p-0">
            <Row className="g-0">

                <AdminSidebar
                    collapsed={collapsed}
                    setCollapsed={setCollapsed}
                />

                <Col
                    style={{
                        marginLeft: collapsed ? 70 : 240,
                        transition: "all 0.3s"
                    }}
                >
                    <AdminHeader />

                    <div className="p-4">
                        <Outlet />
                    </div>
                </Col>

            </Row>
        </Container>
    );
};

export default AdminLayout;