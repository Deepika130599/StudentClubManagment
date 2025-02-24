import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { useSelector } from 'react-redux';
import "./components/userPage.css";
import "./App.css";
import Dashboard from "./components/dashboard";
import Sidebar from "./components/sidebar";
import ClubsList from "./components/clubslist";
import Requests from "./components/requests";
import AnnouncementsPage from "./components/announcementsPage";
import Signin from "./components/Signin";
import Register from "./components/register";

function App() {
  const userLoggedIn = useSelector((state) => !!state.auth.token);

  return (
    <Router>
      {!userLoggedIn ? (
        <Routes>
          <Route path="/signin" element={<Signin />} />
          <Route path="/signup" element={<Register />} />
          <Route path="*" element={<Navigate to="/signin" />} />  {/* Redirect to login if not logged in */}
        </Routes>
      ) : (
        <div className="main-container">
          <Sidebar />
          <div className="container">
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/clubslist" element={<ClubsList />} />
              <Route path="/requests" element={<Requests />} />
              <Route path="/announcements" element={<AnnouncementsPage />} />
              <Route path="*" element={<Navigate to="/" />} /> {/* Redirect unknown routes to Dashboard */}
            </Routes>
          </div>
        </div>
      )}
    </Router>
  );
}

export default App;
