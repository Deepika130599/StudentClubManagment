package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.studentclubmanagement.dtos.ApiResponseDTO;
import org.studentclubmanagement.dtos.ClubDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.models.Club;
import org.studentclubmanagement.services.ClubService;

import java.util.List;

@RestController
@RequestMapping("/club")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Club APIs", description = "APIs for managing clubs, including creating, updating, retrieving, and deleting club records.")
public class ClubController {

    @Autowired
    private ClubService clubService;

    /**
     * Retrieves a list of all clubs.
     *
     * @return List of ClubDTOs wrapped in ApiResponseDTO.
     */
    @GetMapping
    @Operation(
        summary = "Get All Clubs",
        description = "Fetches a list of all clubs available in the system."
    )
    public ResponseEntity<ApiResponseDTO<List<ClubDTO>>> getAllClubs() {
        List<ClubDTO> clubDTOs = clubService.getAllClubs();
        return ResponseEntity.ok(new ApiResponseDTO<>("Clubs retrieved successfully", clubDTOs));
    }

    /**
     * Retrieves a club by its unique ID.
     *
     * @param id The ID of the club.
     * @return The corresponding ClubDTO if found, otherwise a not found response.
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Get Club by ID",
        description = "Fetches details of a specific club by its unique ID."
    )
    public ResponseEntity<ApiResponseDTO<ClubDTO>> getClubById(
        @Parameter(description = "The unique ID of the club to retrieve", required = true, example = "1")
        @PathVariable Long id
    ) {
        try {
            ClubDTO clubDTO = clubService.getClubByIdWithImage(id);
            return ResponseEntity.ok(new ApiResponseDTO<>("Club retrieved successfully", clubDTO));
        } catch (ClubNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("Club not found", null));
        }
    }

    /**
     * Retrieves a club by its name.
     *
     * @param clubName The name of the club.
     * @return The corresponding ClubDTO if found, otherwise a not found response.
     */
    @GetMapping("/getClubByName")
    @Operation(
        summary = "Get Club by Name",
        description = "Fetches details of a specific club by its name."
    )
    public ResponseEntity<ApiResponseDTO<ClubDTO>> getClubByName(
        @Parameter(description = "The name of the club to retrieve", required = true, example = "Science Club")
        @RequestParam String clubName
    ) {
        try {
            ClubDTO clubDTO = clubService.findClubByNameWithImage(clubName);
            return ResponseEntity.ok(new ApiResponseDTO<>("Club retrieved successfully", clubDTO));
        } catch (ClubNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("Club not found with the name: " + clubName, null));
        }
    }

    /**
     * Creates a new club.
     *
     * @param clubName The name of the club.
     * @param description The club's description.
     * @param noOfMembers The current number of members.
     * @param availableSlots The number of available slots for new members.
     * @param totalSlots The total slots available in the club.
     * @param adminId The ID of the club admin.
     * @param image The club's image (optional).
     * @return The newly created ClubDTO.
     */
    @PostMapping(consumes = "multipart/form-data")
    @Operation(
        summary = "Create a New Club",
        description = "Allows the creation of a new club with details including name, description, slots, admin, and an optional image."
    )
    public ResponseEntity<ApiResponseDTO<ClubDTO>> createClub(
            @RequestParam("clubName") String clubName,
            @RequestParam("description") String description,
            @RequestParam("noOfMembers") int noOfMembers,
            @RequestParam("availableSlots") int availableSlots,
            @RequestParam("totalSlots") int totalSlots,
            @RequestParam("adminId") Long adminId,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        ClubDTO clubDTO = new ClubDTO();
        clubDTO.setClubName(clubName);
        clubDTO.setDescription(description);
        clubDTO.setNoOfMembers(noOfMembers);
        clubDTO.setAvailableSlots(availableSlots);
        clubDTO.setTotalSlots(totalSlots);
        clubDTO.setAdminId(adminId);
        clubDTO.setImage(image);

        ClubDTO savedClubDTO = clubService.createClubWithImage(clubDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("Club created successfully", savedClubDTO));
    }

    /**
     * Updates an existing club.
     *
     * @param id The ID of the club to be updated.
     * @param clubName The new name of the club.
     * @param description The updated description.
     * @param noOfMembers The updated number of members.
     * @param availableSlots The updated number of available slots.
     * @param totalSlots The updated total slots.
     * @param adminId The updated club admin ID.
     * @param image The updated club image (optional).
     * @return The updated ClubDTO.
     */
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @Operation(
        summary = "Update an Existing Club",
        description = "Updates club details such as name, description, slots, and optionally its image."
    )
    public ResponseEntity<ApiResponseDTO<ClubDTO>> updateClub(
        @Parameter(description = "The unique ID of the club to update", required = true, example = "1")
        @PathVariable Long id,
        @RequestParam("clubName") String clubName,
        @RequestParam("description") String description,
        @RequestParam("noOfMembers") int noOfMembers,
        @RequestParam("availableSlots") int availableSlots,
        @RequestParam("totalSlots") int totalSlots,
        @RequestParam("adminId") Long adminId,
        @RequestParam(value = "image", required = false) MultipartFile image) throws ClubNotFoundException {

        ClubDTO clubDTO = new ClubDTO();
        clubDTO.setClubName(clubName);
        clubDTO.setDescription(description);
        clubDTO.setNoOfMembers(noOfMembers);
        clubDTO.setAvailableSlots(availableSlots);
        clubDTO.setTotalSlots(totalSlots);
        clubDTO.setAdminId(adminId);

        return ResponseEntity.ok(
                new ApiResponseDTO<>("Club updated successfully",
                        clubService.updateClubFromDTO(id, clubDTO, image))
        );
    }

    /**
     * Deletes a club by its ID.
     *
     * @param id The ID of the club to delete.
     * @return A success or error message.
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a Club",
        description = "Deletes a club from the system by its ID."
    )
    public ResponseEntity<ApiResponseDTO<String>> deleteClub(
        @Parameter(description = "The unique ID of the club to delete", required = true, example = "1")
        @PathVariable Long id
    ) {
        try {
            clubService.deleteClub(id);
            return ResponseEntity.ok(new ApiResponseDTO<>("Club with ID " + id + " has been successfully deleted.", null));
        } catch (ClubNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("Error: Club with ID " + id + " not found.", null));
        }
    }
}
