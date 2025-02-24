import React, { useState } from "react";
import { Link } from "react-router-dom";
import { FaBars, FaHome, FaUsers, FaClipboardList, FaSignOutAlt } from "react-icons/fa";
import styles from "./sidebar.module.css";
import { useDispatch } from "react-redux";
import { logout } from "../state/auth/authSlice";
import { useNavigate } from "react-router-dom";

const Sidebar = () => {
  const dispatch = useDispatch();
  const [isExpanded, setIsExpanded] = useState(false);
  const navigate = useNavigate();

  const onLogout = () => {
    dispatch(logout());
  };

  return (
    <div className={`${styles.sidebar} ${isExpanded ? styles.expanded : ""}`}>
      <div className={isExpanded ? styles.burgerIcon : styles.burgerIconExpanded} onClick={() => setIsExpanded(!isExpanded)}>
        <FaBars />
        {isExpanded && <span>Student Club</span>}
      </div>

      <ul className={styles.sidebarMenu}>
        <li>
          <Link to="/" className={styles.link}>
            <FaHome className={styles.icon} />
            {isExpanded && <span>Dashboard</span>}
          </Link>
        </li>
        <li>
          <Link to="/clubslist" className={styles.link}>
            <FaUsers className={styles.icon} />
            {isExpanded && <span>Clubs List</span>}
          </Link>
        </li>
        <li>
          <Link to="/requests" className={styles.link}>
            <FaClipboardList className={styles.icon} />
            {isExpanded && <span>Requests</span>}
          </Link>
        </li>
      </ul>

      <div className={styles.logoutSection}>
        <button className={styles.logoutButton} onClick={onLogout}>
          <FaSignOutAlt className={styles.icon} />
          {isExpanded && <span>Logout</span>}
        </button>
      </div>
    </div>
  );
};

export default Sidebar;
