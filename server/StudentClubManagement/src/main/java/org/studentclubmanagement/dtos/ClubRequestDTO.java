package org.studentclubmanagement.dtos;

public class ClubRequestDTO {
    private Long userId;
    private Long clubId;
    private String comment;

    public ClubRequestDTO() {}

    public ClubRequestDTO(Long userId, Long clubId, String comment) {
        this.userId = userId;
        this.clubId = clubId;
        this.comment = comment;
    }

    // Getters & Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
