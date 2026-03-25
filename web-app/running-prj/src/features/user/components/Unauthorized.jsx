import React from "react";
import { Button, Container } from "react-bootstrap";
import { NavLink } from "react-router-dom";

const Unauthorized = () => {
    return (
        <section
            className="d-flex align-items-center bg-light"
            style={{ minHeight: "calc(100vh - 140px)" }}
        >
            <Container className="text-center py-5">
                <div
                    className="mx-auto mb-4 d-flex align-items-center justify-content-center rounded-circle"
                    style={{
                        width: 96,
                        height: 96,
                        background: "#ffe5e5",
                        color: "#dc3545",
                        fontSize: 40,
                        fontWeight: 700,
                    }}
                >
                    403
                </div>

                <h1 className="fw-bold mb-3">Bạn không có quyền truy cập</h1>
                <p className="text-muted mx-auto mb-4" style={{ maxWidth: 560 }}>
                    Tài khoản của bạn không được phép mở trang này. Hãy quay lại trang phù hợp
                    hoặc đăng nhập bằng tài khoản có quyền truy cập.
                </p>

                <div className="d-flex justify-content-center gap-3 flex-wrap">
                    <Button as={NavLink} to="/" variant="dark">
                        Về trang chủ
                    </Button>
                    <Button as={NavLink} to="/login" variant="outline-dark">
                        Đăng nhập lại
                    </Button>
                </div>
            </Container>
        </section>
    );
};

export default Unauthorized;
