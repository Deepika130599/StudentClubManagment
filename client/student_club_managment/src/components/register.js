import React, { useState } from "react";
import styles from "./register.module.css"; // Importing module CSS
import { FaEye, FaEyeSlash } from "react-icons/fa";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { createUser } from "../data";

const Register = (props) => {
  const [showPassword, setShowPassword] = useState(false);
  const [formDataErrors, setFormDataErrors] = useState({});
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    phone: "",
    address: "",
    role: "STUDENT",
    birthday: null,
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const togglePasswordVisibility = () => {
    setShowPassword((prevShowPassword) => !prevShowPassword);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    const validData = await validate(formData);
    if (Object.keys(validData).length < 1) {
      await createUser(formData);
    } else {
      setFormDataErrors(validData);
    }
  };

  const validate = (values) => {
    const errors = {};
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i;
    if (!values.firstName) errors.firstName = "First name is required";
    if (!values.lastName) errors.lastName = "Last name is required";
    if (!values.email) {
      errors.email = "Email is required";
    } else if (!regex.test(values.email)) {
      errors.email = "Invalid email format!";
    }
    if (!values.password) {
      errors.password = "Password is required";
    } else if (values.password.length < 8) {
      errors.password = "Password must be at least 8 characters";
    }
    if (!values.birthday) errors.birthday = "Birthday is required";
    if (values.phone.length !== 10) {
      errors.phone = "Please enter a valid 10-digit phone number";
    }
    return errors;
  };

  const handleBirthday = (e) => {
    setFormData({ ...formData, birthday: e });
  };

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>Sign Up</h2>
      <form onSubmit={handleSubmit} className={styles.form}>
        <div className={styles.row}>
          <input type="text" name="firstName" placeholder="First Name" onChange={handleChange} className={styles.inputHalf} required />
          <input type="text" name="lastName" placeholder="Last Name" onChange={handleChange} className={styles.inputHalf} required />
        </div>

        <input type="email" name="email" placeholder="Email" onChange={handleChange} className={styles.input} required />
        <input type="tel" name="phone" placeholder="Phone" onChange={handleChange} pattern="\d{10}" className={styles.input} required />

        <div className={styles.passwordWrapper}>
          <input type={showPassword ? "text" : "password"} name="password" placeholder="Password" onChange={handleChange} className={styles.input} required />
          <span className={styles.eyeIcon} onClick={togglePasswordVisibility}>
            {showPassword ? <FaEyeSlash /> : <FaEye />}
          </span>
        </div>

        <select name="role" onChange={handleChange} className={styles.dropdown}>
          <option value="STUDENT">Student</option>
          <option value="ADMIN">Admin</option>
          <option value="SUPER_ADMIN">Super Admin</option>
        </select>

        <DatePicker
          className={styles.birthdayInput}
          selected={formData.birthday}
          onChange={handleBirthday}
          dateFormat="yyyy-MM-dd"
          maxDate={new Date()}
          showYearDropdown
          showMonthDropdown
          dropdownMode="select"
          placeholderText="Select your birthday"
          required
        />

        <button type="submit" className={styles.button}>Sign Up</button>

        <p className={styles.centerText}>
          Already have an account? <a href="/signin">Sign In</a>
        </p>
      </form>
    </div>
  );
};

export default Register;
