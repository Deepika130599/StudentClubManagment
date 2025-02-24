import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import styles from "./announcementsPage.module.css";
import { FaArrowLeft, FaBullhorn, FaCheckCircle, FaRegCircle } from "react-icons/fa"; // Icons
import { fetchAnnouncements } from '../data'
import "../colors";

const AnnouncementsPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const [announcements, setAnnouncements] = useState([]);
  const [activeTab, setActiveTab] = useState("announcements");
  const clubData = location.state?.clubData;

  useEffect(() => {

    const fetchAnnouncment = async () => {
      try {
        const announcement = await fetchAnnouncements(clubData.clubId);
        setAnnouncements(announcement.data)
      }
      catch (error){
        console.error("Error fetching announcmens:", error);
      }
    }

    fetchAnnouncment()
  }, [clubData.clubId]);

  return (
    <div className={styles.announcementsContainer}>
      <div className={styles.headerContainer}>
        <FaArrowLeft className={styles.backIcon} onClick={() => navigate(-1)} />
        <h2 className={styles.pageTitle}>📢 {clubData?.clubName} Announcements</h2>
      </div>

      <div className={styles.tabsContainer}>
        <button
          className={`${styles.tabButton} ${activeTab === "announcements" ? styles.activeTab : ""}`}
          onClick={() => setActiveTab("announcements")}
        >
          Announcements
        </button>
        <button
          className={`${styles.tabButton} ${activeTab === "qa" ? styles.activeTab : ""}`}
          onClick={() => setActiveTab("qa")}
        >
          Q&A
        </button>
      </div>

      {activeTab === "announcements" ? (
        <div className={styles.announcementsList}>
          {announcements?.length > 0 ? (
            announcements.map((announcement, index) => (
              <div key={announcement.id} className={styles.announcementItem}>
                <div className={styles.announcementContent}>
                  <p className={styles.announcementTitle}>
                    <FaBullhorn /> {announcement.title}
                  </p>
                  <p className={styles.announcementDescription}>{announcement.content}</p>
                </div>

                <p className={styles.announcementDate}>
                  📅 {new Date(announcement.createdAt).toLocaleDateString()}
                </p>
              </div>
            ))
          ) : (
            <p className={styles.noAnnouncements}>No announcements available.</p>
          )}
        </div>
      ) : (
        <div className={styles.qaSection}>Q&A Section is Pending...</div>
      )}
    </div>
  );
};

export default AnnouncementsPage;
