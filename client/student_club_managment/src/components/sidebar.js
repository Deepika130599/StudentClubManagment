import React, { useState } from "react";
import { Link } from "react-router-dom";
import { FaBars, FaHome, FaUsers, FaClipboardList, FaSignOutAlt } from "react-icons/fa";
import "./sidebar.css";

const Sidebar = ({ setUserLoggedIn }) => {
  const [isCollapsed, setIsCollapsed] = useState(false);

  const onLogout = () => {
    setUserLoggedIn(false);
  };

  return (
    <div className={`sidebar ${isCollapsed ? "collapsed" : ""}`}>
      {/* Toggle Button */}
      {/* <button className="sidebar-toggle" onClick={() => setIsCollapsed(!isCollapsed)}>
        <FaBars />
      </button> */}

      {/* Sidebar Content */}
      <h2 className={`sidebar-title ${isCollapsed ? "hide" : ""}`}>Student Club</h2>
      
      <ul className="sidebar-menu">
        <li>
          <Link to="/" className="link">
            {/* <FaHome className="icon" />  */}
            {!isCollapsed && "Dashboard"}
          </Link>
        </li>
        <li>
          <Link to="/clubslist" className="link">
            {/* <FaUsers className="icon" />  */}
            {!isCollapsed && "Clubs List"}
          </Link>
        </li>
        <li>
          <Link to="/requests" className="link">
            {/* <FaClipboardList className="icon" />  */}
            {!isCollapsed && "Requests"}
          </Link>
        </li>
      </ul>

      {/* Logout Button */}
      <div className="logout-section">
        <button className="logout-button" onClick={onLogout}>
          {/* <FaSignOutAlt className="icon" />  */}
          {!isCollapsed && "Logout"}
        </button>
      </div>
    </div>
  );
};

export default Sidebar;
