package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.studentclubmanagement.dtos.ClubRequestDTO;
import org.studentclubmanagement.dtos.ApiResponseDTO;
import org.studentclubmanagement.exceptions.ClubCapacityExceededException;
import org.studentclubmanagement.exceptions.ClubLimitExceededException;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.exceptions.RequestAlreadyExistsException;
import org.studentclubmanagement.models.ClubRequest;
import org.studentclubmanagement.services.ClubRequestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/club-requests")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Club Request APIs", description = "Operations related to requests for joining clubs.")
public class ClubRequestController {

    private final ClubRequestService clubRequestService;

    public ClubRequestController(ClubRequestService clubRequestService) {
        this.clubRequestService = clubRequestService;
    }

    /**
     * Create a new Club Request
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ClubRequestDTO>> createClubRequest(@RequestBody ClubRequestDTO clubRequestDTO)
            throws ClubNotFoundException, ClubLimitExceededException, RequestAlreadyExistsException {

        ClubRequest savedRequest = clubRequestService.createClubRequest(clubRequestDTO);

        // Convert to DTO before sending response
        ClubRequestDTO responseDTO = new ClubRequestDTO(savedRequest.getUser().getUserId(),
                savedRequest.getClub().getClubId(),
                savedRequest.getComment());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("Club request submitted successfully", responseDTO));
    }

    /**
     * Get all pending club requests for a specific club (Admin view)
     */
    @GetMapping("/{clubId}")
    public ResponseEntity<ApiResponseDTO<List<ClubRequestDTO>>> getClubRequests(@PathVariable Long clubId) {
        List<ClubRequest> requests = clubRequestService.getClubRequestsByClubId(clubId);

        // Convert to DTOs
        List<ClubRequestDTO> responseDTOs = requests.stream()
                .map(req -> new ClubRequestDTO(req.getUser().getUserId(),
                        req.getClub().getClubName(),
                        req.getClub().getClubId(),
                        req.getComment(),
                        req.getStatus(),
                        req.getCreatedAt(),
                        req.getUpdatedAt()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponseDTO<>("Club requests fetched successfully", responseDTOs));
    }

    /**
     * Get all pending club requests for a specific club (Admin view)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponseDTO<List<ClubRequestDTO>>> getClubRequestsByUser(@PathVariable Long userId) {
        List<ClubRequest> requests = clubRequestService.getClubRequestsByUserId(userId);

        // Convert to DTOs
        List<ClubRequestDTO> responseDTOs = requests.stream()
                .map(req -> new ClubRequestDTO(req.getUser().getUserId(),
                        req.getClub().getClubName(),
                        req.getClub().getClubId(),
                        req.getComment(),
                        req.getStatus(),
                        req.getCreatedAt(),
                        req.getUpdatedAt()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponseDTO<>("Club requests fetched successfully", responseDTOs));
    }

    /**
     * Approve a Club Request (Admin action)
     */
    @PutMapping("/{requestId}/approve")
    public ResponseEntity<ApiResponseDTO> approveClubRequest(@PathVariable Long requestId) throws ClubLimitExceededException, ClubCapacityExceededException, RequestAlreadyExistsException {
        clubRequestService.approveClubRequest(requestId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Club request approved successfully", null));
    }

    /**
     * Reject a Club Request (Admin action)
     */
    @PutMapping("/{requestId}/reject")
    public ResponseEntity<ApiResponseDTO> rejectClubRequest(@PathVariable Long requestId) {
        clubRequestService.rejectClubRequest(requestId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Club request rejected successfully", null));
    }
}
