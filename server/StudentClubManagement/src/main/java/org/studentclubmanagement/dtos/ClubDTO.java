package org.studentclubmanagement.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class ClubDTO {

    @NotBlank(message = "Club Name is mandatory")
    @Size(max = 100, message = "Club Name must be less than 100 characters")
    private String clubName;

    @NotBlank(message = "Club Description is mandatory")
    @Size(max = 500, message = "Club Description must be less than 500 characters")
    private String description;

    @Min(value = 0, message = "Number of members cannot be negative")
    private int noOfMembers = 0;

    @Min(value = 1, message = "Available slots must be at least 1")
    private int availableSlots;

    @Min(value = 1, message = "Total slots must be at least 1")
    private int totalSlots;

    @NotNull(message = "Admin ID is mandatory")
    private Long adminId;
    // ClubAdmin Details
    private String clubAdminFirstName;
    private String clubAdminLastName;


    // Handle image as MultipartFile for upload
    private MultipartFile image;

    // New Field for Base64-encoded Image URL (For GET APIs)
    private String imageUrl;

    // Constructors
    public ClubDTO() {}

    public ClubDTO(String clubName, String description, int noOfMembers, int availableSlots, int totalSlots, Long adminId) {
        this.clubName = clubName;
        this.description = description;
        this.noOfMembers = noOfMembers;
        this.availableSlots = availableSlots;
        this.totalSlots = totalSlots;
        this.adminId = adminId;
    }

    // Getters and Setters
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

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getClubAdminFirstName() {
        return clubAdminFirstName;
    }

    public void setClubAdminFirstName(String clubAdminFirstName) {
        this.clubAdminFirstName = clubAdminFirstName;
    }

    public String getClubAdminLastName() {
        return clubAdminLastName;
    }

    public void setClubAdminLastName(String clubAdminLastName) {
        this.clubAdminLastName = clubAdminLastName;
    }
}
