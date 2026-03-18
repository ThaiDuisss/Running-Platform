import React, { useState } from "react";
import { authService } from "../services/AuthService";
import "@/style/register.css"

function Register() {
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

  return (
    <div className="rg-wrap">
      <div className="rg-container">

        {/* LEFT IMAGE */}
        <div className="rg-left"></div>

        {/* RIGHT FORM */}
        <div className="rg-right">
          <form className="rg-form" onSubmit={handleSubmit}>
            <h2>Register</h2>

            <input
              type="text"
              name="username"
              placeholder="Gmail"
              value={formData.username}
              onChange={handleChange}
              className="rg-input"
            />
            {error.username && <p className="rg-error">{error.username}</p>}

            <input
              type="text"
              name="fullName"
              placeholder="Full Name"
              value={formData.fullName}
              onChange={handleChange}
              className="rg-input"
            />
            {error.fullName && <p className="rg-error">{error.fullName}</p>}

            <input
              type="text"
              name="phoneNumber"
              placeholder="Phone Number"
              value={formData.phoneNumber}
              onChange={handleChange}
              className="rg-input"
            />
            {error.phoneNumber && <p className="rg-error">{error.phoneNumber}</p>}

            <input
              type="password"
              name="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
              className="rg-input"
            />
            {error.password && <p className="rg-error">{error.password}</p>}

            <input
              type="password"
              name="confirmPassword"
              placeholder="Confirm Password"
              value={formData.confirmPassword}
              onChange={handleChange}
              className="rg-input"
            />
            {error.confirmPassword && <p className="rg-error">{error.confirmPassword}</p>}

            <button type="submit" className="rg-button">
              Register
            </button>

            <a href="/login" className="rg-link">
              Already have an account? Login
            </a>

            {success && <p className="rg-success">{success}</p>}
          </form>
        </div>

      </div>
    </div>
  );
}

const styles = {
  container: {
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    height: "100vh",
    backgroundColor: "#f4f4f4"
  },
  form: {
    background: "white",
    padding: "30px",
    borderRadius: "10px",
    width: "350px",
    boxShadow: "0 0 10px rgba(0,0,0,0.1)"
  },
  input: {
    width: "100%",
    padding: "10px",
    margin: "10px 0",
    borderRadius: "5px",
    border: "1px solid #ccc"
  },
  button: {
    width: "100%",
    padding: "10px",
    backgroundColor: "#007bff",
    color: "white",
    border: "none",
    borderRadius: "5px",
    cursor: "pointer"
  },
  error: {
    color: "red"
  },
  success: {
    color: "green"
  }
};

export default Register;