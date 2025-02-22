import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { loginUser, fetchUserDetails } from "../state/auth/authSlice";
import { useNavigate } from "react-router-dom";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import "../colors";
import "./Signin.css";

const SignIn = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [signInErrors, setSignInErrors] = useState({});
  const [signInData, setSignInData] = useState({ email: "", password: "" });
  const [showPassword, setShowPassword] = useState(false);
  const { isLoading, error } = useSelector((state) => state.auth);

  useEffect(() => {
    console.log("Updated sign-in data:", signInData);
  }, [signInData]);

  const validateSignIn = (values) => {
    const errors = {};
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i;

    if (!values.email) errors.email = "Email is required";
    else if (!emailRegex.test(values.email)) errors.email = "Invalid email format";

    if (!values.password) errors.password = "Password is required";
    else if (values.password.length < 8) errors.password = "Password must be at least 8 characters";

    return errors;
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setSignInData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleSignInSubmit = async (event) => {
    event.preventDefault();
    const validationErrors = validateSignIn(signInData);
    if (Object.keys(validationErrors).length > 0) {
      setSignInErrors(validationErrors);
      return;
    }

    try {
      const response = await dispatch(loginUser(signInData)).unwrap();
      if (response.email === signInData.email) {
        await dispatch(fetchUserDetails(signInData.email));
        navigate("/");
      }
    } catch (error) {
      console.error("Login failed:", error);
      setSignInErrors({ general: "Login failed. Please try again." });
    }
  };

  return (
    <div className="signin-container">
      <h2 className="signin-title">Log in</h2>
      <form className="signin-form" onSubmit={handleSignInSubmit}>
        <label>Email</label>
        <input type="email" name="email" placeholder="Enter your email" onChange={handleInputChange} className="input-field" />
        {signInErrors.email && <p className="error-text">{signInErrors.email}</p>}
        
        <label>Password</label>
        <div className="password-container">
          <input type={showPassword ? "text" : "password"} name="password" placeholder="Enter your password" onChange={handleInputChange} className="input-field" />
          <span className="eye-icon" onClick={() => setShowPassword(!showPassword)}>
            {showPassword ? <FaEyeSlash /> : <FaEye />}
          </span>
        </div>
        {signInErrors.password && <p className="error-text">{signInErrors.password}</p>}

        {signInErrors.general && <p className="error-text">{signInErrors.general}</p>}
        <button type="submit" className="signin-button">Log in</button>
      </form>
      <p className="signup-text">Don’t have an account? <a href="/signup">Sign up</a></p>
    </div>
  );
};

export default SignIn;
