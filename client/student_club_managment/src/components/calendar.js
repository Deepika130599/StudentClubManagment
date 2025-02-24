import React from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import styles from "./calendar.module.css"; // Updated for module-based CSS import

const events = [
  { date: "20th", description: "Google Marketing campaign" },
  { date: "21st", description: "Restock product with 80 new" },
  { date: "23rd", description: "Meeting with suppliers" },
];

const CalendarSection = () => {
  return (
    <div className={styles.calendarContainer}>
      <h3 className={styles.calendarTitle}>Calendar</h3>
      <Calendar className={styles.calendarWidget} />
      <div className={styles.upcomingEvents}>
        <div className={styles.eventsHeader}>
          <h3 className={styles.eventsTitle}>📌 Upcoming Events</h3>
          <span className={styles.eventsMore}>...</span>
        </div>
        <p className={styles.eventsSubtitle}>{events.length} activities</p>

        <ul className={styles.eventsList}>
          {events.map((event, index) => (
            <li key={index} className={styles.eventItem}>
              <p className={styles.eventDate}>{event.date}</p>
              <p className={styles.eventDescription}>{event.description}</p>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default CalendarSection;