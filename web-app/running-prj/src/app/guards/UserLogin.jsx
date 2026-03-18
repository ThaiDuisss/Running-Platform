import React, { useContext, useState } from 'react'
import "@/style/signin.css"
import { NavLink, useNavigate, useSearchParams } from 'react-router-dom';

import {
    FaUser,
    FaLock,
    FaEye,
    FaEyeSlash,
    FaFacebookF,
} from "react-icons/fa";
import { Form, Button, InputGroup, Alert } from "react-bootstrap";
import { FcGoogle } from "react-icons/fc";
import { AuthActionContext } from '../providers/AuthProvider';
import { authService } from '../services/AuthService';
const UserLogin = () => {
    const [formData, setFormData] = useState({
        username: "",
        password: "",
        rememberMe: ""
    });
    const [message, setMessage] = useState("");
    const [showPassword, setShowPassWord] = useState(false);
    const navigate = useNavigate();
    const { login } = useContext(AuthActionContext)
    const [searchParams] = useSearchParams();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await login(formData)
            navigate("/")
        } catch (error) {
            setMessage(
                error?.response?.data?.message ||
                error?.message ||
                "Login failed"
            );
        }
    }

    const handleOnChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }))
    }

    const handleLoginWithSocialPlatform = (type) => {
        try {
            authService.signinWithSocialPlatform(type, searchParams);
        } catch (error) {
            setMessage(`Failed to login with ${type}. Please try again.`);
        }
    }

    return (
        <div className="lp-wrap">
            <div className="lp-container">

                {/* LEFT SIDE (IMAGE) */}
                <div className="lp-left">

                </div>

                {/* RIGHT SIDE (LOGIN) */}
                <div className="lp-right">
                    <div className="lp-form">

                        <h2 className="lp-title">Welcome</h2>

                        {message && <Alert variant='danger'>{message}</Alert>}

                        <Form onSubmit={handleSubmit}>

                            {/* USERNAME */}
                            <InputGroup className="lp-group">
                                <InputGroup.Text>
                                    <FaUser />
                                </InputGroup.Text>
                                <Form.Control
                                    placeholder='Enter username or email'
                                    type='text'
                                    name="username"
                                    value={formData.username}
                                    onChange={handleOnChange}
                                    className='lp-control'
                                />
                            </InputGroup>

                            {/* PASSWORD */}
                            <InputGroup className="lp-group">
                                <InputGroup.Text>
                                    <FaLock />
                                </InputGroup.Text>

                                <Form.Control
                                    type={showPassword ? "text" : "password"}
                                    placeholder="Enter your password"
                                    className="lp-control"
                                    name='password'
                                    value={formData.password}
                                    onChange={handleOnChange}
                                />

                                <Button
                                    type='button'
                                    className='lp-eye'
                                    onClick={() => setShowPassWord(v => !v)}
                                >
                                    {showPassword ? <FaEyeSlash /> : <FaEye />}
                                </Button>
                            </InputGroup>

                            {/* REMEMBER */}
                            <div className='d-flex justify-content-between mb-3'>
                                <Form.Check
                                    name='rememberMe'
                                    label="Remember me"
                                />
                                <NavLink className="lp-link" to="/forgot-password">
                                    Forgot password
                                </NavLink>
                            </div>

                            {/* LOGIN BUTTON */}
                            <Button type='submit' className='lp-login-btn'>
                                Login
                            </Button>

                        </Form>

                        {/* REGISTER */}
                        <div className='text-center mt-3 lp-muted'>
                            Don't have an account?{" "}
                            <NavLink to="/register">Sign up</NavLink>
                        </div>

                        {/* DIVIDER */}
                        <div className='lp-divider'>OR</div>

                        {/* SOCIAL */}
                        <div className="d-flex justify-content-center gap-3">
                            <button
                                type="button"
                                className="lp-social lp-social-fb"
                                onClick={() => handleLoginWithSocialPlatform("facebook")}
                            >
                                <FaFacebookF />
                            </button>

                            <button
                                type="button"
                                className="lp-social lp-social-gg"
                                onClick={() => handleLoginWithSocialPlatform("google")}
                            >
                                <FcGoogle />
                            </button>
                        </div>

                    </div>
                </div>

            </div>
        </div>
    )
}

export default UserLogin