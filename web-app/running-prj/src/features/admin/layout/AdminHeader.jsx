
import { AuthActionContext, AuthDataContext } from '@/app/providers/AuthProvider';
import React, { useContext } from 'react';
import { Navbar, Nav, NavDropdown, Image } from 'react-bootstrap';
import { Link, NavLink } from 'react-router-dom';

const AdminHeader = () => {

    const { user, theme } = useContext(AuthDataContext);
    const { logout } = useContext(AuthActionContext);

    return (
        <>
            {/* CSS inline để bỏ mũi tên */}
            <style>
                {`
                    #admin-user-dropdown::after {
                        display: none !important;
                    }
                `}
            </style>

            <div className='border-bottom'>
                <Navbar
                    expand="lg"
                    className={`px-4 py-2 ${theme === "Dark" ? "navbar-dark bg-dark" : "navbar-light bg-white"}`}
                >

                    {/* Logo */}
                    <Navbar.Brand
                        as={Link}
                        to="/"
                        className="d-flex align-items-center fw-bold fs-3"
                    >
                        <img
                            src="/logo/logo-running.png"
                            style={{ height: 25, width: 30, paddingRight: 10 }}
                        />
                        <span style={{ fontSize: "18px" }}>RunWise</span>
                        <span
                            className='ms-1 rounded-circle'
                            style={{
                                backgroundColor: "#6f4ef6",
                                height: 6,
                                width: 6,
                                display: "inline-block"
                            }}
                        />
                    </Navbar.Brand>

                    <Navbar.Toggle />

                    <Navbar.Collapse>

                        {!user ? (
                            <div className="d-flex gap-2 ms-auto">
                                <NavLink className="btn btn-outline-primary" to="/login">
                                    Đăng nhập
                                </NavLink>

                                <NavLink className="btn btn-outline-primary" to="/register">
                                    Đăng ký
                                </NavLink>
                            </div>
                        ) : (

                            <Nav className="ms-auto">

                                <NavDropdown
                                    align="end"
                                    id="admin-user-dropdown"
                                    title={
                                        <div className="d-flex align-items-center gap-2">

                                            <Image
                                                src={user?.imageUrl || "/default-avatar.png"}
                                                roundedCircle
                                                style={{
                                                    width: 32,
                                                    height: 32,
                                                    objectFit: "cover"
                                                }}
                                            />

                                            <span style={{ fontWeight: 500 }}>
                                                {user?.username}
                                            </span>

                                        </div>
                                    }
                                >

                                    <NavDropdown.Item>
                                        Profile
                                    </NavDropdown.Item>

                                    <NavDropdown.Item>
                                        Settings
                                    </NavDropdown.Item>

                                    <NavDropdown.Divider />

                                    <NavDropdown.Item onClick={logout}>
                                        Logout
                                    </NavDropdown.Item>

                                </NavDropdown>

                            </Nav>

                        )}

                    </Navbar.Collapse>

                </Navbar>
            </div>
        </>
    );
};

export default AdminHeader;
