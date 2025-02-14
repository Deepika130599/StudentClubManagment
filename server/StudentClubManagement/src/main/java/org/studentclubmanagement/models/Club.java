package org.studentclubmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clubId;

    @NotBlank(message = "Club name is mandatory")
    @Size(max = 100, message = "Club name must be less than 100 characters")
    @Column(unique = true)
    private String clubName;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @Min(value = 0, message = "Number of members cannot be negative")
    private int noOfMembers = 0;

    @Min(value = 1, message = "Available slots must be at least 1")
    private int availableSlots;

    @Min(value = 1, message = "Total slots must be at least 1")
    private int totalSlots;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "club_admin_user_id")
    private User clubAdmin;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserClub> userClubs;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Announcement> announcements;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClubRequest> requests;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Question> questions;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Answer> answers;

    @JsonIgnore
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClubAdminRequest> clubAdminRequests;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters


    public User getClubAdmin() {
        return clubAdmin;
    }

    public void setClubAdmin(User clubAdmin) {
        this.clubAdmin = clubAdmin;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNoOfMembers() {
        return noOfMembers;
    }

    public void setNoOfMembers(int noOfMembers) {
        this.noOfMembers = noOfMembers;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<UserClub> getUserClubs() {
        return userClubs;
    }

    public void setUserClubs(Set<UserClub> userClubs) {
        this.userClubs = userClubs;
    }

    public Set<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(Set<Announcement> announcements) {
        this.announcements = announcements;
    }

    public Set<ClubRequest> getRequests() {
        return requests;
    }

    public void setRequests(Set<ClubRequest> requests) {
        this.requests = requests;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Set<ClubAdminRequest> getClubAdminRequests() {
        return clubAdminRequests;
    }

    public void setClubAdminRequests(Set<ClubAdminRequest> clubAdminRequests) {
        this.clubAdminRequests = clubAdminRequests;
    }
}
