import { AuthActionContext, AuthDataContext } from '@/app/providers/AuthProvider';
import React, { useState, useEffect, useContext } from 'react';
import { Nav, NavDropdown } from 'react-bootstrap';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { FiBell } from "react-icons/fi";

import "@/style/header.css";
import { FaChevronDown } from 'react-icons/fa6';
const RunWiseNavbar = () => {
    const { user, theme } = useContext(AuthDataContext);
    const navigator = useNavigate();
    const { logout } = useContext(AuthActionContext);
    const navLinkCustom = ({ isActive }) =>
        "nav-link " +
        (isActive
            ? "border-bottom border-black text-info"
            : "text-gray");

    return (

        <div className=' border-bottom '>
            <nav
                className={`navbar navbar-expand-lg container py-2 ${theme === "Dark" ? "navbar-dark bg-dark" : "navbar-light bg-white"
                    }`}
            >
                <Link
                    to="/"
                    className="d-flex align-items-center fw-bold navbar-brand fs-3"
                >
                    <img src="/logo/logo-running.png" style={{ height: 35, width: 40, paddingRight: 10 }} />
                    RunWise
                    <span className='ms-1 rounded-circle'
                        style={{ backgroundColor: "#6f4ef6", height: 6, width: 6, display: "inline-block" }}>
                    </span>
                </Link>
                <button type='button' className='navbar-toggler'
                    data-bs-toggle="collapse"
                    data-bs-target="#mainNavbar">
                    <span className='navbar-toggler-icon'></span>
                </button>
                <div className="collapse navbar-collapse" id='mainNavbar'>
                    <ul className='navbar-nav mx-auto mb-2 mb-lg-0 gap-lg-4'>
                        <li className="nav-item">
                            <NavLink className={navLinkCustom} end to="/">Home</NavLink>
                        </li>
                        <li className="nav-item">
                            <NavLink className={navLinkCustom} to="/routes">Tuyến đường</NavLink>
                        </li>
                        <li className="nav-item">
                            <NavLink className={navLinkCustom} to="/feed">Cộng đồng</NavLink>
                        </li>
                        <li className="nav-item">
                            <NavLink className={navLinkCustom} to="/plans">Lên kế hoạch</NavLink>
                        </li>
                        <li className="nav-item">
                            <NavLink className={navLinkCustom} to="/blog">Tin tức</NavLink>
                        </li>
                        <li className="nav-item">
                            <NavLink className={navLinkCustom} to="/chat">Trò chuyện</NavLink>
                        </li>
                    </ul>
                </div>
                {user === null || user === undefined || Object.keys(user).length === 0 ?
                    (<div className="d-flex gap-2 ms-5">
                        <NavLink className="btn btn-outline-primary me-2" to="/login">Đăng nhập</NavLink>
                        <NavLink className="btn btn-outline-primary" to="/register">Đăng ký</NavLink>
                    </div>) :

                    (< div className="header-right">

                        <div className="icon-btn notification">
                            <FiBell size={25} className="icon-bell" />
                            <span className="badge">3</span>
                        </div>
                        <div className="avatar-wrapper">
                            <div className="sub-avatar-wrapper">
                                <div className="avatar-shield" onClick={() => setOpen(!open)}>
                                    <img src={user.imageUrl} />
                                </div>
                                <FaChevronDown color='rgba(65, 19, 125, 0.33)' />
                            </div>


                            <div className="dropdown">
                                <div className='sub-dropdown'>
                                    <div onClick={() => navigator("/profile")}>Profile</div>
                                </div>
                                <div className='sub-dropdown' >
                                    <div onClick={logout}>Logout</div>
                                </div>
                            </div>
                        </div>
                        <div />
                    </div>
                    )

                }

            </nav >
        </div >

    );
};

export default RunWiseNavbar;