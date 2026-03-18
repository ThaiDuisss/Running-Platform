import { useState } from "react";
import { useSearchParams, NavLink } from "react-router-dom";
import { Container, Row, Col, Card, Form, Button, InputGroup, Alert } from "react-bootstrap";
import { FaLock, FaEye, FaEyeSlash } from "react-icons/fa";
import { authService } from "../services/AuthService";

function ResetPassword() {
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");

  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [showPassword, setShowPassword] = useState(false);

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      setError("Passwords do not match");
      setMessage("");
      return;
    }

    try {
      const res = await authService.resetPassword({
        token,
        newPassword: password
      });

      setMessage(res.message || "Password reset successful");
      setError("");
      setPassword("");
      setConfirmPassword("");

    } catch (err) {
      console.error(err);
      setError(err.response?.data?.message || "Invalid or expired token");
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
                  <h2 className="lp-title">Reset Password</h2>

                  {message && <Alert variant="success">{message}</Alert>}
                  {error && <Alert variant="danger">{error}</Alert>}
                </Card.Title>

                <Form onSubmit={handleSubmit}>

                  {/* New Password */}
                  <InputGroup className="mb-3">
                    <InputGroup.Text>
                      <FaLock />
                    </InputGroup.Text>

                    <Form.Control
                      type={showPassword ? "text" : "password"}
                      placeholder="Enter new password"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      className="lp-control"
                      required
                    />

                    <Form.Control
                      type={showPassword ? "text" : "password"}
                      placeholder="Enter confirm password"
                      value={confirmPassword}
                      onChange={(e) => setConfirmPassword(e.target.value)}
                      className="lp-control"
                      required
                    />

                    <Button
                      variant="link"
                      className="lp-eye"
                      onClick={() => setShowPassword(!showPassword)}
                    >
                      {showPassword ? <FaEyeSlash /> : <FaEye />}
                    </Button>
                  </InputGroup>

                  <Button type="submit" className="lp-login-btn w-100">
                    Reset Password
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

export default ResetPassword;