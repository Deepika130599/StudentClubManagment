package org.studentclubmanagement.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ClubAdminRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    @ManyToOne
    @JoinColumn(name = "club_admin_id", nullable = false)
    private User clubAdmin;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Enumerated(EnumType.STRING)
    private RequestStatus clubAdminApprovalStatus;

    @Enumerated(EnumType.STRING)
    private RequestStatus superAdminApprovalStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public User getClubAdmin() {
        return clubAdmin;
    }

    public void setClubAdmin(User clubAdmin) {
        this.clubAdmin = clubAdmin;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public RequestStatus getClubAdminApprovalStatus() {
        return clubAdminApprovalStatus;
    }

    public void setClubAdminApprovalStatus(RequestStatus clubAdminApprovalStatus) {
        this.clubAdminApprovalStatus = clubAdminApprovalStatus;
    }

    public RequestStatus getSuperAdminApprovalStatus() {
        return superAdminApprovalStatus;
    }

    public void setSuperAdminApprovalStatus(RequestStatus superAdminApprovalStatus) {
        this.superAdminApprovalStatus = superAdminApprovalStatus;
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
}
