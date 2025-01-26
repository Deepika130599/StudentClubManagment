import React, {useState} from "react"
import './colors'
import './Signin.css'
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { FaEye, FaEyeSlash } from "react-icons/fa";

const Signin = () => {

    const [isSignIn, setIsSignIn] = useState(true)
    const [showPassword, setShowPassword] = useState(false);
    const [errors, setErrors] = useState({});
    const [errorsSignIn, setErrorsSignIn] = useState({})
    const [formData, setFormData] = useState({
        first_name: "",
        last_name: "",
        email: "",
        password: "",
        birthday: null, 
    });
    const [formDataSignIn, setFormDataSignIn] = useState({
        user_name: "",
        password: ""
    })

    

    const handleSignInUsername = (e) => {
        e.preventDefault();
        // setFormDataSignIn((prevData) => {
        //     ...prevData
        // })
    }

    const handleSignInPassword = (e) => {
        e.preventDefault();
    }

    const changeToSignup = (e) => {
        e.preventDefault();
        setIsSignIn(true)
    }

    const changeToSignin = (e) => {
        // setIsSignIn(false)
        e.preventDefault();
        setIsSignIn(false)
    }

    const handleDateChange = (date) => {
        // setFormData((prevData) => ({
        //     ...prevData,
        //     birthday: date,
        // }));
        console.log("handle date change")
    };

    const handleFirstName = (event) => {
        console.log("event.target.value")
    }

    const handleLastName = (event) => {
        console.log("event.target.value")
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        console.log("handle submit button")
    };

    const togglePasswordVisibility = () => {
        setShowPassword((prevShowPassword) => !prevShowPassword);
    };

    const handleSigninSubmit = (event) => {
        console.log("handle Sign in submit")
    }




    return (
        <div className="background">
            <div className="card">
                
                    {
                        isSignIn ? 
                        (<div className="sign-up-form">
                            <h2 className="text-element">Sign In</h2>
                            <form className="form" onSubmit={handleSigninSubmit}>
                            
                            <div>
                                <input type="text" placeholder="User Name" className = "input-field" required />
                            </div>
                            
                            <div>
                                <input type="password" placeholder="Password"className = "input-field" required />
                            </div>
                            <button type="submit" className="btn" >Sign In</button>
                            </form>
                            
                            <p><a className="reroute_text" href="https://www.w3schools.com" > Forgot Password ?</a> </p>
                            <p>Not a member yet? <a  className="reroute_text"  onClick={changeToSignin}>Sign Up</a></p>
                        </div>
                        )
                        :
                        (
                        <div className="sign-up-form">
                            <h2 className="text-element">Register</h2>
                            
                            <form>
                            <div className="input-field-container">
                                <div> <input type="text" placeholder="First Name" onClick={handleFirstName} className="input-field input-field-size" required />  </div>
                                <div> <input type="text" placeholder="Last Name" onClick={handleLastName} className="input-field input-field-size" required />  </div>
                            </div>

                            <div ><input type="email" placeholder="Email" className="input-field" required /> </div>
                            <div > <input type={showPassword ? "text" : "password"} placeholder="Password" className="input-field" required />
                            <span
                                onClick={togglePasswordVisibility}
                                
                            >
                                {showPassword ? <FaEyeSlash className="icon" /> : <FaEye className="icon" />}
                            </span> </div>
                            
                            <div>
                                <DatePicker
                                    className="input-field date-picker"
                                    selected={formData.birthday}
                                    onChange={handleDateChange}
                                    dateFormat="yyyy-MM-dd"
                                    maxDate={new Date()}
                                    showYearDropdown
                                    showMonthDropdown
                                    dropdownMode="select"
                                    placeholderText="Select your birthday"
                                    required
                                />
                            </div>
                            <button type="submit" className="btn" >Sign Up</button>
                            <p><a  className="reroute_text"  onClick={changeToSignup}>Sign In</a></p>
                            </form>
                        </div>
                        )
                    }
            </div>
        </div>
    )
}

export default Signin