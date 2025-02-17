import React, { useState, useEffect } from "react";
import "../colors";
import "./Signin.css";
import "react-datepicker/dist/react-datepicker.css";
import { fetchUser } from "../data";
import Register from "./register";
import "./userPage.css";

const SignIn = (props) => {
    const [isSigningIn, setIsSigningIn] = useState(true);
    const [signInErrors, setSignInErrors] = useState({});
    const [signInData, setSignInData] = useState({
        email: "manohar9@gmail.com",
        password: "manohar123",
    });

    // Log sign-in data on update (for debugging)
    useEffect(() => {
        console.log("Updated sign-in data:", signInData);
    }, [signInData]);

    // Validate user input
    const validateSignIn = (values) => {
        const errors = {};
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i;

        if (!values.email) {
            errors.email = "Email is required";
        } else if (!emailRegex.test(values.email)) {
            errors.email = "Invalid email format";
        }

        if (!values.password) {
            errors.password = "Password is required";
        } else if (values.password.length < 8) {
            errors.password = "Password must be at least 8 characters";
        }

        return errors;
    };

    // Handle input changes
    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setSignInData((prevData) => ({ ...prevData, [name]: value }));
    };

    // Handle form submission
    const handleSignInSubmit = async (event) => {
        event.preventDefault();
        const validationErrors = validateSignIn(signInData);

        if (Object.keys(validationErrors).length > 0) {
            setSignInErrors(validationErrors);
            return;
        }

        try {
            const userData = await fetchUser(signInData);
            if(userData.data){
                props.setUserLoggedIn(true)
                
            }
            
            console.log("User signed in:", userData);
        } catch (error) {
            console.error("Login failed:", error);
            setSignInErrors({ general: "Login failed. Please try again." });
        }
    };

    // Toggle between Sign In and Sign Up mode
    const toggleSignInMode = () => {
        setIsSigningIn(!isSigningIn);
    };

    return (
        <div className="background">
            <div className="card">
                {isSigningIn ? (
                    <div className="sign-in-form">
                        <h2 className="text-element">Sign In</h2>
                        <form className="form" onSubmit={handleSignInSubmit}>
                            <div>
                                <input 
                                    type="email" 
                                    name="email" 
                                    placeholder="Email" 
                                    onChange={handleInputChange} 
                                    className="input-field" 
                                    // required 
                                />
                                {signInErrors.email && <p className="error-text">{signInErrors.email}</p>}
                            </div>
                            <div>
                                <input 
                                    type="password" 
                                    name="password" 
                                    placeholder="Password" 
                                    onChange={handleInputChange} 
                                    className="input-field" 
                                    // required 
                                />
                                {signInErrors.password && <p className="error-text">{signInErrors.password}</p>}
                            </div>
                            {signInErrors.general && <p className="error-text">{signInErrors.general}</p>}
                            <button type="submit" className="btn">Sign In</button>
                        </form>
                        <p><a className="reroute-text" href="#">Forgot Password?</a></p>
                        <p>Not a member yet? <a className="reroute-text" onClick={toggleSignInMode}>Sign Up</a></p>
                    </div>
                ) : (
                    <Register isSigningIn={isSigningIn} toggleSignInMode={setIsSigningIn} />
                )}
            </div>
        </div>
    );
};

export default SignIn;
