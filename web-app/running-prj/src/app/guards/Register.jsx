import React, { useState } from "react";
import { authService } from "../services/AuthService";
import { Container, Row, Col, Card, Form, Button, InputGroup, Alert } from "react-bootstrap";
import { FaUser, FaLock, FaPhone, FaEnvelope, FaEye, FaEyeSlash } from "react-icons/fa";
import { NavLink } from "react-router-dom";
import "@/style/signin.css";

const RegisterPage = () => {
  const [formData, setFormData] = useState({
    username: "",
    fullName: "",
    phoneNumber: "",
    password: "",
    confirmPassword: ""
  });

  const [error, setError] = useState({});
  const [success, setSuccess] = useState("");

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    setError("");
    setSuccess("");

    if (!handleComparePassword()) {
      return;
    }
    try {
      await authService.register(formData);
      setSuccess("The verification link has been sent to your email address. Please check your email.");
      setFormData({
        username: "",
        fullName: "",
        phoneNumber: "",
        password: "",
        confirmPassword: ""
      });
    } catch (err) {
      const res = err.response?.data;
      if (res?.data && Array.isArray(res.data)) {
        const errors = {};
        res.data.forEach(e => {
          errors[e.field] = e.message;
        });
        setError(errors); // object lỗi
      } else {
        setError(res?.message || "Registration failed");
      }
    }
  };

  const handleComparePassword = () => {
    if (formData.password !== formData.confirmPassword) {
      console.log("confirmPassword do not match");
      setError({ confirmPassword: "Passwords do not match" });
      return false;
    }
    return true;
  };

  const [showPassword, setShowPassword] = useState(false);

  return (
    <div className="lp-wrap">
      <Container>
        <Row className="justify-content-center">
          <Col xs={12} sm={10} md={8} lg={6} xl={5}>
            <Card>
              <Card.Body className="lp-body border-0 shadow-sm">

                <Card.Title>
                  <h2 className="lp-title">Register</h2>

                  {success && <Alert variant="success">{success}</Alert>}
                  {error.general && <Alert variant="danger">{error.general}</Alert>}
                </Card.Title>

                <Form onSubmit={handleSubmit}>

                  {/* Username / Email */}
                  <InputGroup className="mb-3">
                    <InputGroup.Text><FaEnvelope /></InputGroup.Text>
                    <Form.Control
                      name="username"
                      placeholder="Email"
                      value={formData.username}
                      onChange={handleChange}
                      required
                    />
                  </InputGroup>
                  {error.username && <p className="text-danger">{error.username}</p>}

                  {/* Full Name */}
                  <InputGroup className="mb-3">
                    <InputGroup.Text><FaUser /></InputGroup.Text>
                    <Form.Control
                      name="fullName"
                      placeholder="Full Name"
                      value={formData.fullName}
                      onChange={handleChange}
                      required
                    />
                  </InputGroup>
                  {error.fullName && <p className="text-danger">{error.fullName}</p>}

                  {/* Phone */}
                  <InputGroup className="mb-3">
                    <InputGroup.Text><FaPhone /></InputGroup.Text>
                    <Form.Control
                      name="phoneNumber"
                      placeholder="Phone Number"
                      value={formData.phoneNumber}
                      onChange={handleChange}
                      required
                    />
                  </InputGroup>
                  {error.phoneNumber && <p className="text-danger">{error.phoneNumber}</p>}

                  {/* Password */}
                  <InputGroup className="mb-3">
                    <InputGroup.Text><FaLock /></InputGroup.Text>

                    <Form.Control
                      type={showPassword ? "text" : "password"}
                      name="password"
                      placeholder="Password"
                      value={formData.password}
                      onChange={handleChange}
                      required
                    />

                    <Form.Control
                      type={showPassword ? "text" : "password"}
                      name="confirmPassword"
                      placeholder="Confirm Password"
                      value={formData.confirmPassword}
                      onChange={handleChange}
                      required
                    />

                    <Button
                      variant="link"
                      onClick={() => setShowPassword(!showPassword)}
                    >
                      {showPassword ? <FaEyeSlash /> : <FaEye />}
                    </Button>
                  </InputGroup>
                  {error.password && <p className="text-danger">{error.password}</p>}
                  {error.confirmPassword && <p className="text-danger">{error.confirmPassword}</p>}

                  <Button type="submit" className="w-100">
                    Register
                  </Button>

                </Form>

                <div className="text-center mt-3">
                  Already have an account?{" "}
                  <NavLink to="/login">Login</NavLink>
                </div>

              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default RegisterPage;
