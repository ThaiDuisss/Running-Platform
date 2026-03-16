import React, { useContext, useState } from 'react'
import "@/style/signin.css"
import { NavLink, useNavigate, useSearchParams } from 'react-router-dom';

import {
    FaUser,
    FaLock,
    FaEye,
    FaEyeSlash,
    FaFacebookF,
    FaGithub,
} from "react-icons/fa";
import {
    Container,
    Row,
    Col,
    Card,
    Form,
    Button,
    InputGroup,
    Alert,
} from "react-bootstrap";
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
            console.error(error);
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
            console.error(`Error during ${type} login:`, error);
            setMessage(`Failed to login with ${type}. Please try again.`);
        }
    }
    return (
        <div className="lp-wrap">
            <Container>
                <Row className="justify-content-center">
                    <Col xs={12} sm={10} md={8} lg={6} xl={5}>
                        <Card>
                            <Card.Body className="lp-body border-0 shadow-sm">
                                <Card.Title>
                                    <h2 className="lp-title">Login Page</h2>
                                    {message && <Alert variant='danger'>{message}</Alert>}                                </Card.Title>
                                <Form onSubmit={handleSubmit}>
                                    <InputGroup>
                                        <InputGroup.Text className="lp-icon-left">
                                            <FaUser />
                                        </InputGroup.Text>
                                        <Form.Control
                                            placeholder='Enter username or email'
                                            type='text'
                                            name="username"
                                            autoComplete="username"
                                            value={formData.username}
                                            onChange={handleOnChange}
                                            className='lp-control' />

                                    </InputGroup>
                                    <InputGroup>
                                        <InputGroup.Text>
                                            <FaLock></FaLock>
                                        </InputGroup.Text>
                                        <Form.Control
                                            type={showPassword ? "text" : "password"} placeholder="Enter your password"
                                            className="lp-control"
                                            name='password'
                                            value={formData.password}
                                            onChange={handleOnChange} />
                                        <Button type='button'
                                            variant='link'
                                            className='lp-eye'
                                            onClick={() => setShowPassWord((v) => !v)}
                                            aria-label={showPassword ? "Show password" : "Hide password"}
                                        >
                                            {showPassword ? <FaEyeSlash /> : <FaEye />}
                                        </Button>
                                    </InputGroup>
                                    <div className='d-flex align-item-center justify-content-between mb-3'>
                                        <Form.Check
                                            value={formData.rememberMe}
                                            name='rememberMe'
                                            label="Remember me" />
                                        <NavLink className="lp-link" to="/forgot-password">Forget your password</NavLink>
                                    </div>
                                    <Button type='submit' className='lp-login-btn w-100'>
                                        Login
                                    </Button>
                                </Form>
                                <div className='text-center mt-3 lp-muted'>
                                    Don't have an account? {" "}
                                    <NavLink to="/register">Sign up</NavLink>
                                </div>
                                <div className='text-center my-3 lp-divider'>
                                    or connect width
                                </div>
                                <div className="d-flex justify-content-center gap-4">
                                    <button
                                        type="button"
                                        className="lp-social lp-social-fb"
                                        aria-label="Continue with Facebook"
                                        onClick={() => handleLoginWithSocialPlatform("facebook")}
                                    >
                                        <FaFacebookF />
                                    </button>
                                    <button
                                        type="button"
                                        className="lp-social lp-social-gg"
                                        aria-label="Continue with Google"
                                        onClick={() => handleLoginWithSocialPlatform("google")}
                                    >
                                        <FcGoogle />
                                    </button>
                                </div>
                            </Card.Body>

                        </Card>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}

export default UserLogin
