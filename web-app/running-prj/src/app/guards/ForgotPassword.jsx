import { useState } from "react";
import { Container, Row, Col, Card, Form, Button, InputGroup, Alert } from "react-bootstrap";
import { FaEnvelope } from "react-icons/fa";
import { NavLink } from "react-router-dom";
import { authService } from "../services/AuthService";

function ForgotPassword() {
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await authService.forgotPassword(email);

      setMessage(res.message || "Check your email for reset link");
      setError("");
      setEmail("");

    } catch (err) {
      setError(err.response?.data?.message || "Failed to send reset link");
      setMessage("");
    }
  };

  return (
    <div className="lp-wrap">
      <Container>
        <Row className="justify-content-center">
          <Col xs={12} sm={10} md={8} lg={6} xl={5}>
            <Card>
              <Card.Body className="lp-body border-0 shadow-sm">

                <Card.Title>
                  <h2 className="lp-title">Forgot Password</h2>

                  {message && <Alert variant="success">{message}</Alert>}
                  {error && <Alert variant="danger">{error}</Alert>}
                </Card.Title>

                <Form onSubmit={handleSubmit}>
                  <InputGroup className="mb-3">
                    <InputGroup.Text className="lp-icon-left">
                      <FaEnvelope />
                    </InputGroup.Text>

                    <Form.Control
                      type="email"
                      placeholder="Enter your email"
                      value={email}
                      autoComplete="email"
                      onChange={(e) => setEmail(e.target.value)}
                      className="lp-control"
                      required
                    />
                  </InputGroup>

                  <Button type="submit" className="lp-login-btn w-100">
                    Send Reset Link
                  </Button>
                </Form>

                <div className="text-center mt-3 lp-muted">
                  Remember your password?{" "}
                  <NavLink to="/login">Login</NavLink>
                </div>

              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
}

export default ForgotPassword;