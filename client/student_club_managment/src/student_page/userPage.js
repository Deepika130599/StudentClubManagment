import React from "react";
import { FaHome, FaUser, FaCog } from "react-icons/fa";
import "./userPage.css";

const UserPage = () => {

    return (
        <div className="sidebar">
            <div className="sidebar-header">
                <img src="https://via.placeholder.com/40" alt="Logo" />
                <h2>Student Club</h2>
            </div>

            <nav className="sidebar-nav">
                <a href="#dashboard">
                    <FaHome />
                    Dashboard
                </a>
                <a href="#clubs">
                    <FaUser />
                    Clubs
                </a>
                <a href="#requests">
                    <FaCog />
                    Requests
                </a>
            </nav>
        </div>
    )

}

export default UserPage