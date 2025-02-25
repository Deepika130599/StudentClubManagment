package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.studentclubmanagement.dtos.ApiResponseDTO;
import org.studentclubmanagement.dtos.ClubDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.models.Club;
import org.studentclubmanagement.services.ClubService;

import java.util.List;

@RestController
@RequestMapping("/api/club")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Club APIs", description = "Operations related to clubs.")
public class ClubController {

    @Autowired
    private ClubService clubService;

    /**
     * Retrieves a list of all clubs.
     *
     * @return List of ClubDTOs wrapped in ApiResponseDTO.
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ClubDTO>>> getAllClubs() {
        List<ClubDTO> clubDTOs = clubService.getAllClubs();
        return ResponseEntity.ok(new ApiResponseDTO<>("Clubs retrieved successfully", clubDTOs));
    }

    /**
     *
     * @param id
     * @return ClubDTO by clubId
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ClubDTO>> getClubById(@PathVariable Long id) {
        try {
            ClubDTO clubDTO = clubService.getClubByIdWithImage(id);
            return ResponseEntity.ok(new ApiResponseDTO<>("Club retrieved successfully", clubDTO));
        } catch (ClubNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("Club not found", null));
        }
    }

    /**
     *
     * @param clubName
     * @return ClubDTO by clubName
     */
    @GetMapping("/getClubByName")
    public ResponseEntity<ApiResponseDTO<ClubDTO>> getClubByName(@RequestParam String clubName) {
        try {
            ClubDTO clubDTO = clubService.findClubByNameWithImage(clubName);
            return ResponseEntity.ok(new ApiResponseDTO<>("Club retrieved successfully", clubDTO));
        } catch (ClubNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("Club not found with the name: " + clubName, null));
        }
    }

    /**
     * Creates a new club in the club entity.
     *
     * @param clubName
     * @param description
     * @param noOfMembers
     * @param availableSlots
     * @param totalSlots
     * @param adminId
     * @param image
     * @return ClubDTO of the newly created club.
     */
    @PostMapping(consumes = "multipart/form-data")
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
     * @param id
     * @param
     * @return Updated ClubDTO.
     */
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponseDTO<ClubDTO>> updateClub(
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
     * Deletes a club.
     *
     * @param id
     * @return Deletion status message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> deleteClub(@PathVariable Long id) {
        try {
            clubService.deleteClub(id);
            return ResponseEntity.ok(new ApiResponseDTO<>("Club with ID " + id + " has been successfully deleted.", null));
        } catch (ClubNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("Error: Club with ID " + id + " not found.", null));
        }
    }
}
