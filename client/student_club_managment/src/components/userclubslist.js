import "../colors";
import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from "react-router-dom";
import styles from "./userclubslist.module.css"; // Ensure correct module import
import { getUserClubs } from "../data";
import moment from "moment"; // For formatting dates

const UserClubsList = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [userClubs, setUserClubs] = useState([]);
  const userData = useSelector((state) => state.auth.user);

  useEffect(() => {
    if (userData?.data[0]?.userClubs) {
      setUserClubs(userData.data[0].userClubs);
    }
  }, [userData]);

  const handleSelectClub = (club) => {
    navigate(`/announcements`, { state: { clubData: club } });
  };

  return (
    <div className={styles.pageContainer}>
      <h2 className={styles.pageTitle}>📋 Clubs List</h2>
      {userClubs.length > 0 ? (
        <div className={styles.clubCardContainer}>
          {userClubs.map((clubData, index) => {
            const club = clubData.club;
            return (
              <div 
                key={index} 
                className={styles.clubCard} 
                onClick={() => handleSelectClub(club)}
              >
                <div className={styles.clubCardHeader}>
                  <h3 className={styles.clubTitle}>{club.clubId}. {club.clubName}</h3>
                  <span className={`${styles.status} ${clubData.terminationDate ? styles.inactive : styles.active}`}>
                    {clubData.terminationDate ? "Terminated" : "Active"}
                  </span>
                </div>
                <p className={styles.clubDescription}>{club.description}</p>
                <div className={styles.clubSlots}>
                  <p>👥 Members: <strong>{club.noOfMembers}</strong></p>
                  <p>✅ Available: <strong>{club.availableSlots}</strong></p>
                  <p>📌 Total: <strong>{club.totalSlots}</strong></p>
                </div>
                <p className={styles.clubJoined}>
                  🗓️ Joined: <strong>{moment(clubData.joinedDate).format("MMM DD, YYYY")}</strong>
                </p>
              </div>
            );
          })}
        </div>
      ) : (
        <div className={styles.userClubsBg}>No clubs found</div>
      )}
    </div>
  );
};

export default UserClubsList;
