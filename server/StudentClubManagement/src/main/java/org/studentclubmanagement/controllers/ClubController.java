package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.studentclubmanagement.dtos.ApiResponseDTO;
import org.studentclubmanagement.dtos.ClubDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.models.Club;
import org.studentclubmanagement.services.ClubService;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Club APIs", description = "Operations related to clubs")
public class ClubController {

    @Autowired
    private ClubService clubService;

    /**
     * Retrieves a list of all clubs.
     *
     * @return List of Clubs wrapped in ApiResponseDTO.
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<Club>>> getAllClubs() {
        List<Club> clubs = clubService.getAllClubs();
        return ResponseEntity.ok(new ApiResponseDTO<>("Clubs retrieved successfully", clubs));
    }

    /**
     *
     * @param id
     * @return Club by clubId
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getClubById(@PathVariable Long id) {
        try {
            Club club = clubService.getClubById(id);
            return ResponseEntity.ok(new ApiResponseDTO("Club retrieved successfully", club));
        } catch (ClubNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO("Club not found", null));
        }
    }

    /**
     *
     * @param clubName
     * @return
     */
    @GetMapping("/getClubByName")
    public ResponseEntity<ApiResponseDTO> getClubByName(@RequestParam String clubName) {
        try {
            Club club = clubService.findClubByName(clubName);
            return ResponseEntity.ok(new ApiResponseDTO("Club retrieved successfully", club));
        } catch (ClubNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("Club not found with the name: "+clubName,null));
        }
    }

    /**
     *
     * Creates a new club in the club entity.
     * @param clubDTO
     * @return Club
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Club>> createClub(@Valid @RequestBody ClubDTO clubDTO) {
        Club savedClub = clubService.createClub(clubDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("Club created successfully", savedClub));
    }

    /**
     *
     * @param id
     * @param updatedClubDTO
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateClub(@PathVariable Long id,
                                                     @Validated @RequestBody ClubDTO updatedClubDTO) {
        try {
            Club updatedClub = clubService.updateClubFromDTO(id, updatedClubDTO);
            return ResponseEntity.ok(new ApiResponseDTO("Club updated successfully", updatedClub));
        } catch (ClubNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO("Error: Club with ID " + id + " not found", null));
        }
    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteClub(@PathVariable Long id) {
        try {
            clubService.deleteClub(id);
            return ResponseEntity.ok(new ApiResponseDTO("Club with ID " + id + " has been successfully deleted.",null));
        } catch (ClubNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO("Error: Club with ID " + id + " not found.",null));
        }
    }
}
