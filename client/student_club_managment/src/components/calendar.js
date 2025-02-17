import React from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import "./calendar.css"; // Import styles

const events = [
  { date: "20th", description: "Google Marketing campaign" },
  { date: "21st", description: "Restock product with 80 new" },
  { date: "23rd", description: "Meeting with suppliers" },
];

const CalendarSection = () => {
  return (
    <div className="calendar-container">
      <h3 className="calendar-title">Calendar</h3>
      <Calendar className="calendar-widget" />
      <div className="upcoming-events">
        <div className="events-header">
          <h3 className="events-title">📌 Upcoming Events</h3>
          <span className="events-more">...</span>
        </div>
        <p className="events-subtitle">{events.length} activities</p>

        <ul className="events-list">
          {events.map((event, index) => (
            <li key={index} className="event-item">
              <p className="event-date">{event.date}</p>
              <p className="event-description">{event.description}</p>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default CalendarSection;
