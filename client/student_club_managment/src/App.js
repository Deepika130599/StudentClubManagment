import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./components/userPage.css";
import Dashboard from "./components/dashboard";
import Sidebar from "./components/sidebar";
import ClubsList from "./components/clubslist"
import Requests from "./components/requests"
import Main from "./main"
import "./App.css"
import Signin from "./components/Signin"

function App() {
  const [userLoggedIn, setUserLoggedIn] = useState(false);
  return (
    <div>
    {
      !userLoggedIn ? 
        <div>
          <Signin setUserLoggedIn = {setUserLoggedIn}/>
        </div>
      :
      <Router>
        <div className="main-container">
            <Sidebar  setUserLoggedIn = {setUserLoggedIn} />
            <div className="container">
            <Routes>
                <Route path="/" element={<Dashboard />} />
                <Route path="/clubslist" element={<ClubsList />} />
                <Route path="/requests" element={<Requests />} />
            </Routes>
            </div>
        </div>
    </Router>
    }
    </div>
    
  );
}

export default App;
