import React, { useState } from "react";
import { authService } from "../services/AuthService";

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
    <div style={styles.container}>
      <form style={styles.form} onSubmit={handleSubmit}>
        <h2>Register</h2>

        <input
          type="text"
          name="username"
          placeholder="Gmail"
          value={formData.username}
          onChange={handleChange}
          required
          style={styles.input}
        />
        {error.username && <p className="text-danger">{error.username}</p>}

        <input
          type="text"
          name="fullName"
          placeholder="Full Name"
          value={formData.fullName}
          onChange={handleChange}
          required
          style={styles.input}
        />
        {error.fullName && <p className="text-danger">{error.fullName}</p>}

        <input
          type="text"
          name="phoneNumber"
          placeholder="Phone Number"
          value={formData.phoneNumber}
          onChange={handleChange}
          required
          style={styles.input}
        />
        {error.phoneNumber && <p className="text-danger">{error.phoneNumber}</p>}

        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          required
          style={styles.input}
        />
        {error.password && <p className="text-danger">{error.password}</p>}

        <input
          type="password"
          name="confirmPassword"
          placeholder="Confirm Password"
          value={formData.confirmPassword}
          onChange={handleChange}
          required
          style={styles.input}
        />
        {error.confirmPassword && <p className="text-danger">{error.confirmPassword}</p>}

        <button type="submit" style={styles.button}>
          Register
        </button>
        <a href="/login" style={{ display: "block", marginTop: "10px", textAlign: "center" }}>
          Already have an account? Login
        </a>

        {/* {error && <p style={styles.error}>{error}</p>} */}
        {success && <p style={styles.success}>{success}</p>}
      </form>
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