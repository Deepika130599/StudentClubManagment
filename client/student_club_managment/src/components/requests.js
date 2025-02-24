import React, { useEffect, useState } from "react";
import "../colors"; /* Ensure the correct path */
import styles from "./request.module.css"; // Import CSS module

// Dummy JSON Data
const requestsData = [
  {
    clubName: "Chess Club",
    requestedTime: "2025-02-10 14:30:00",
    status: "Pending",
    comment: "",
    deniedTime: ""
  },
  {
    clubName: "Art Club",
    requestedTime: "2025-02-08 16:00:00",
    status: "Rejected",
    comment: "We are at full capacity. Try again next semester.",
    deniedTime: "2025-02-09 10:15:00"
  },
  {
    clubName: "Music Club",
    requestedTime: "2025-02-12 12:45:00",
    status: "Rejected",
    comment: "Your reason does not align with our club's objectives.",
    deniedTime: "2025-02-13 09:30:00"
  }
];

const Requests = () => {
  const [expandedRequest, setExpandedRequest] = useState(null);

  const toggleExpand = (index) => {
    setExpandedRequest(expandedRequest === index ? null : index);
  };

  return (
    <div className={styles.pageContainer}>
      <h2 className={styles.pageTitle}>My Club Requests</h2>
      <div className={styles.requestsContainer}>
        {requestsData.map((request, index) => (
          <div
            key={index}
            className={`${styles.requestCard} ${request.status === "Rejected" ? styles.rejected : ""}`}
            onMouseEnter={() => toggleExpand(index)}
            onMouseLeave={() => toggleExpand(index)}
          >
            <h3 className={styles.clubName}>{request.clubName}</h3>
            <p className={styles.status}>{request.status}</p>
            <p className={styles.requestedTime}>Requested: {request.requestedTime}</p>
            {request.status === "Rejected" && expandedRequest === index && (
              <div className={styles.rejectionDetails}>
                <p className={styles.deniedText}>Denied</p>
                <p className={styles.comment}><strong>Admin Comment:</strong> {request.comment}</p>
                <p className={styles.deniedTime}>Denied Time: {request.deniedTime}</p>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default Requests;
