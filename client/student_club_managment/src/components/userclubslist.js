import React, { useEffect, useState } from "react";
import "./userclubslist.css"; // Import styles
import { getUserClubs } from "../data";
import moment from "moment"; // For formatting dates

const UserClubsList = () => {
  const [userClubs, setUserClubs] = useState([]);

  const fetchClubs = async () => {
    try {
      const result = await getUserClubs(4);
      setUserClubs(result.data.userClubs);
    } catch {
      console.error("Error fetching user clubs");
    }
  };

  useEffect(() => {
    fetchClubs();
  }, []);

  return (
    <div>
      <h2 className="clubs-title">📋 Clubs List</h2>
      {userClubs.length > 0 ? (
        <div className="club-card-container">
          {userClubs.map((clubData, index) => {
            const club = clubData.club;
            return (
              <div key={index} className="club-card">
                {/* Club Header */}
                <div className="club-card-header">
                  <h3 className="club-title">{club.clubName}</h3>
                  <span className={`status ${clubData.terminationDate ? "inactive" : "active"}`}>
                    {clubData.terminationDate ? "Terminated" : "Active"}
                  </span>
                </div>

                {/* Club Description */}
                <p className="club-description">{club.description}</p>

                {/* Slots Info */}
                <div className="club-slots">
                  <p>👥 Members: <strong>{club.noOfMembers}</strong></p>
                  <p>✅ Available: <strong>{club.availableSlots}</strong></p>
                  <p>📌 Total: <strong>{club.totalSlots}</strong></p>
                </div>

                {/* Joined Date */}
                <p className="club-joined">
                  🗓️ Joined: <strong>{moment(clubData.joinedDate).format("MMM DD, YYYY")}</strong>
                </p>
              </div>
            );
          })}
        </div>
      ) : (
        <div className="user-clubs-bg">No clubs found</div>
      )}
    </div>
  );
};

export default UserClubsList;
