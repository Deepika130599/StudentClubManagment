import React from "react";
import UserClubsList from "./userclubslist";
import CalendarSection from "./calendar";
import "./dashboard.css"; // Import styles

const Dashboard = () => {
  return (
    <div className="dashboard-container">
      <div className="dashboard-left">
        <UserClubsList />
      </div>
      <div className="dashboard-right">
        <CalendarSection />
      </div>
    </div>
  );
};

export default Dashboard;
