import { AuthActionContext, AuthDataContext } from '@/app/providers/AuthProvider';
import React, { useContext } from 'react';
import { Navbar, Nav, NavDropdown } from 'react-bootstrap';
import { Link, NavLink } from 'react-router-dom';

const AdminHeader = () => {

    const { user, theme } = useContext(AuthDataContext);
    const { logout } = useContext(AuthActionContext);

    return (
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
                        style={{ height: 35, width: 40, paddingRight: 10 }}
                    />
                    RunWise
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
                                title={user?.name || "Account"}
                                align="end"
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
    );
};

export default AdminHeader;