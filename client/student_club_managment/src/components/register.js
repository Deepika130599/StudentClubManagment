import React, {useState} from "react"
import '../colors'
import { FaEye, FaEyeSlash, FaPhone } from "react-icons/fa";
import DatePicker from "react-datepicker";
import {createUser} from '../data'


const Register = (props) => {
    const [showPassword, setShowPassword] = useState(false);
    const [formDataErrors, setFormDataErrors] = useState({})
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        phone: 0,
        address: "",
        role: "STUDENT",
        birthday: null, 
    });

    const handleChange = (e) => {
        console.log("from the console", e.target, " values ", e.target.value)
        const {name, value} = e.target
        setFormData({...formData, [name]: value})
        console.log(formData)
    }


    const togglePasswordVisibility = () => {
        setShowPassword((prevShowPassword) => !prevShowPassword);
    };

    const handleSubmit = async(event) => {
        event.preventDefault();
        const validData = await validate(formData)
        let submitData = {}
        if(Object.keys(validData).length < 1){
            submitData = await createUser(formData)
        }
        else{
            setFormDataErrors(validData)
        }
    };

    const validate = (values) => {
        const errors = {}
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i;
        if(!values.firstName){
            errors.firstName = "First name is required"
        }
        if(!values.lastName){
            errors.lastName = "Last name is required"
        }if(!values.email){
            errors.email = "Email is required"
        }else if (!regex.test(values.email)) {
            errors.email = "This is not a valid email format!";
        }
        if(!values.password){
            errors.password = "Password is required"
        }
        else if(values.password.length < 4){
            errors.password = "Password must be more than 4 characters"
        }
        if(!values.birthday){
            errors.birthday = "Birthday is required"
        }
        if(values.phone.length < 10 || values.phone.length >= 11){
            errors.phone = "Please enter 10 digit phone number"
        }
        return errors
    }

    const handleBirthday = (e) => {
        setFormData({...formData, "birthday": e})
    }

    // const changeToSignup = (e) => {
    //     e.preventDefault();
    //     setIsSignIn(true)
    // }
    
    return (
        <div className="sign-up-form">
                <h2 className="text-element">Register</h2>
                
                <form onSubmit={handleSubmit}>
                <div className="input-field-container">
                    <div> <input type="text" name="firstName" placeholder="First Name" onChange={handleChange} className="input-field input-field-size" required />  </div>
                    <div> <input type="text" name="lastName" placeholder="Last Name" onChange={handleChange} className="input-field input-field-size" required />  </div>
                </div>

                <div ><input type="email" name="email" placeholder="Email" className="input-field" onChange={handleChange} required /> </div>
                <div ><input type="tel" name="phone" placeholder="phone" className="input-field" onChange={handleChange} pattern="\d{10}" required /> </div>
                <div > <input type={showPassword ? "text" : "password"} name="password" placeholder="Password" onChange={handleChange} className="input-field" required />
                <span
                    onClick={togglePasswordVisibility}
                    
                >
                    {showPassword ? <FaEyeSlash className="icon" /> : <FaEye className="icon" />}
                </span> </div>
                <div>
                    <select
                        id = "role"
                        onChange={handleChange}
                        className="input-field"
                        name="role"
                        value={formData["role"]}
                    >
                        <option value="STUDENT">Student</option>
                        <option value="ADMIN">Admin</option>
                        <option value="SUPER_ADMIN">Super Admin</option>

                    </select>
                </div>
                <div>
                    <DatePicker
                        className="input-field date-picker"
                        selected={formData.birthday}
                        name="birthday"
                        onChange={handleBirthday}
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
                <p><a  className="reroute_text"  onClick={(event) => {
                        event.preventDefault(); 
                        props.toggleSignInMode(!props.isSigningIn);
                        }}>Sign In</a></p>
                </form>
            </div>
    )

}

export default Register
