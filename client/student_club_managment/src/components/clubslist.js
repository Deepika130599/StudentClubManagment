import React, { useEffect, useState } from "react";
import styles from "./clubslist.module.css"; // Import as a module
import { fetchAllClubs, joinClub, fetchAnnouncements } from "../data"; // Assume API functions exist
import { useSelector } from "react-redux";
import "../colors"; /* Ensure the correct path */

const ClubsList = () => {
  const [clubs, setClubs] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [joinedClubs, setJoinedClubs] = useState(new Set()); // Store joined club IDs
  const [joinReasons, setJoinReasons] = useState({}); // Store input values
  const [selectedClub, setSelectedClub] = useState(null);
  const [announcements, setAnnouncements] = useState([]);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await fetchAllClubs(); // Fetching data
        setClubs(response.data);
      } catch (error) {
        console.error("Error fetching clubs:", error);
      }
    };
    fetchData();
  }, []);

  const handleJoinClub = async (clubId) => {
    if (!joinReasons[clubId] || joinReasons[clubId].trim() === "") {
      alert("Please enter a reason to join the club.");
      return;
    }
    try {
      await joinClub(clubId, joinReasons[clubId]); // API call to join
      setJoinedClubs(new Set([...joinedClubs, clubId])); // Update state
      alert("Join request submitted successfully!");
    } catch (error) {
      console.error("Error joining club:", error);
    }
  };

  const handleSelectClub = async (clubId) => {
    setSelectedClub(clubId);
    try {
      const response = await fetchAnnouncements(clubId);
      // const response = await fetchAnnouncements(clubId, page);
      if (response.data.length === 0) {
        setHasMore(false);
      } else {
        setAnnouncements(response.data);
      }
    } catch (error) {
      console.error("Error fetching announcements:", error);
    }
  };

  return (
    <div className={styles.pageContainer}>
      <input
        type="text"
        placeholder="Search clubs..."
        className={styles.searchBar}
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
      />
      <div className={styles.clubsContainer}>
        {clubs
          .filter((club) =>
            club.clubName.toLowerCase().includes(searchTerm.toLowerCase())
          )
          .map((club) => (
            <div key={club.clubId} className={styles.clubCard} onClick={() => handleSelectClub(club.clubId)}>
              <h3 className={styles.clubName}>{club.clubName}</h3>
              <p className={styles.clubDescription}>{club.description}</p>
              <input
                type="text"
                placeholder="Why do you want to join?"
                className={styles.joinInput}
                value={joinReasons[club.clubId] || ""}
                onChange={(e) =>
                  setJoinReasons({ ...joinReasons, [club.clubId]: e.target.value })
                }
                disabled={joinedClubs.has(club.clubId)}
              />
              <button
                className={styles.joinButton}
                onClick={(e) => {
                  e.stopPropagation();
                  handleJoinClub(club.clubId);
                }}
                disabled={joinedClubs.has(club.clubId)}
              >
                {joinedClubs.has(club.clubId) ? "Joined" : "Join"}
              </button>
            </div>
          ))}
      </div>
    </div>
  );
};

export default ClubsList;
