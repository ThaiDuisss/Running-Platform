import { useState } from "react";
import { Form, Button, InputGroup, Alert } from "react-bootstrap";
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
      console.log(err)
      setError(err.response?.data?.message || "Failed to send reset link");
      setMessage("");
    }
  };

  return (
    <div className="rg-wrap">
      <div className="rg-container">

        {/* LEFT IMAGE */}
        <div className="rg-left"></div>

        {/* RIGHT FORM */}
        <div className="rg-right">
          <div className="rg-form">

            <h2>Forgot Password</h2>

            {message && <Alert variant="success">{message}</Alert>}
            {error && <Alert variant="danger">{error}</Alert>}

            <Form onSubmit={handleSubmit}>

              <InputGroup className="mb-3">
                <InputGroup.Text>
                  <FaEnvelope />
                </InputGroup.Text>

                <Form.Control
                  type="email"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="rg-input"
                  required
                />
              </InputGroup>

              <Button type="submit" className="rg-button">
                Send Reset Link
              </Button>

            </Form>

            <NavLink to="/login" className="rg-link">
              Remember your password? Login
            </NavLink>

          </div>
        </div>

      </div>
    </div>
  );
}

export default ForgotPassword;