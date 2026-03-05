import React from "react";
import { Button } from "react-bootstrap";
import { Bell } from "lucide-react";
import "@/style/plan.css";
const Topbar = () => {
    return (
        <div className="topbar d-flex justify-content-between align-items-center">

            <div>
                <h3 className="fw-bold mb-0">
                    Kế Hoạch Chạy Bộ – 4 Tuần
                </h3>

                <small className="text-muted">
                    Mục tiêu: Chạy 10km trong 8 tuần
                </small>
            </div>

            <div className="d-flex gap-3">

                <Bell />

                <Button variant="primary">
                    + Thêm Buổi
                </Button>

            </div>

        </div>
    );
};

export default Topbar;