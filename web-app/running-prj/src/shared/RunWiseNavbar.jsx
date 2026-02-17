import React, { useState, useEffect } from 'react';
import { Navbar, Nav, Container, Button } from 'react-bootstrap';
import { Link, NavLink } from 'react-router-dom';

const RunWiseNavbar = () => {
    const navLinkCustom = ({ isActive }) => {
        return "nav-link" + (isActive ? "border-bottom border-black text-info" : " text-gray");
    }

    return (

        <div className='container py-2'>
            <nav className="navbar navbar-expand-lg bg-white">
                <a className='d-flex align-items-center fw-bold navbar-brand italic fs-3' href='/'>
                    <img src="/logo/logo-running.png" style={{ height: 35, width: 40, paddingRight: 10 }} />
                    RunWise
                    <span className='ms-1 rounded-circle'
                        style={{ backgroundColor: "#6f4ef6", height: 6, width: 6, display: "inline-block" }}>
                    </span>
                </a>
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
                            <NavLink className={navLinkCustom} to="/community">Cộng đồng</NavLink>
                        </li>
                        <li className="nav-item">
                            <NavLink className={navLinkCustom} to="/plans">Lên kế hoạch</NavLink>
                        </li>
                        <li className="nav-item">
                            <NavLink className={navLinkCustom} to="/blog">Tin tức</NavLink>
                        </li>
                        <li className="nav-item">
                            <NavLink className={navLinkCustom} to="/friends">Bạn bè</NavLink>
                        </li>
                    </ul>
                </div>
            </nav>

        </div>

    );
};

export default RunWiseNavbar;