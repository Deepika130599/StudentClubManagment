package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/club-requests")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Club Request APIs", description = "APIs for managing requests to join clubs, including submitting, retrieving, approving, and rejecting club requests.")
public class ClubRequestController {

    private final ClubRequestService clubRequestService;

    public ClubRequestController(ClubRequestService clubRequestService) {
        this.clubRequestService = clubRequestService;
    }

    /**
     * Creates a new club join request.
     *
     * @param clubRequestDTO The club request details including user ID, club ID, and comment.
     * @return A response entity containing a success message and the created request.
     * @throws ClubNotFoundException If the specified club does not exist.
     * @throws ClubLimitExceededException If the user has reached the maximum number of clubs.
     * @throws RequestAlreadyExistsException If the user has already requested to join this club.
     */
    @PostMapping
    @Operation(
        summary = "Create a Club Request",
        description = "Allows a user to request to join a specific club."
    )
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
     * Retrieves all pending club requests for a specific club (Admin only).
     *
     * @param clubId The unique ID of the club whose requests need to be fetched.
     * @return A response entity containing a list of club requests.
     */
    @GetMapping("/{clubId}")
    @Operation(
        summary = "Get Club Requests by Club ID",
        description = "Fetches all pending club requests for a specific club (Admin only)."
    )
    public ResponseEntity<ApiResponseDTO<List<ClubRequestDTO>>> getClubRequests(
        @Parameter(description = "The unique ID of the club whose requests need to be retrieved", required = true, example = "1")
        @PathVariable Long clubId
    ) {
        List<ClubRequest> requests = clubRequestService.getClubRequestsByClubId(clubId);

        // Convert to DTOs
        return getApiResponseDTOResponseEntity(requests);
    }

    /**
     * Retrieves all club requests made by a specific user.
     *
     * @param userId The unique ID of the user whose club requests need to be fetched.
     * @return A response entity containing a list of club requests made by the user.
     */
    @GetMapping("/user/{userId}")
    @Operation(
        summary = "Get Club Requests by User ID",
        description = "Fetches all club requests made by a specific user."
    )
    public ResponseEntity<ApiResponseDTO<List<ClubRequestDTO>>> getClubRequestsByUser(
        @Parameter(description = "The unique ID of the user whose club requests need to be retrieved", required = true, example = "10")
        @PathVariable Long userId
    ) {
        List<ClubRequest> requests = clubRequestService.getClubRequestsByUserId(userId);

        // Convert to DTOs
        return getApiResponseDTOResponseEntity(requests);
    }

    private ResponseEntity<ApiResponseDTO<List<ClubRequestDTO>>> getApiResponseDTOResponseEntity(List<ClubRequest> requests) {
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
     * Approves a club request (Admin action).
     *
     * @param requestId The unique ID of the club request to approve.
     * @return A success message indicating the approval of the club request.
     * @throws ClubLimitExceededException If the user has already joined the maximum allowed clubs.
     * @throws ClubCapacityExceededException If the club has no available slots.
     * @throws RequestAlreadyExistsException If the user is already a member of the club.
     */
    @PutMapping("/{requestId}/approve")
    @Operation(
        summary = "Approve a Club Request",
        description = "Allows an admin to approve a club join request."
    )
    public ResponseEntity<ApiResponseDTO> approveClubRequest(
        @Parameter(description = "The unique ID of the club request to approve", required = true, example = "5")
        @PathVariable Long requestId
    ) throws ClubLimitExceededException, ClubCapacityExceededException, RequestAlreadyExistsException {
        clubRequestService.approveClubRequest(requestId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Club request approved successfully", null));
    }

    /**
     * Rejects a club request (Admin action).
     *
     * @param requestId The unique ID of the club request to reject.
     * @return A success message indicating the rejection of the club request.
     */
    @PutMapping("/{requestId}/reject")
    @Operation(
        summary = "Reject a Club Request",
        description = "Allows an admin to reject a club join request."
    )
    public ResponseEntity<ApiResponseDTO> rejectClubRequest(
        @Parameter(description = "The unique ID of the club request to reject", required = true, example = "5")
        @PathVariable Long requestId
    ) {
        clubRequestService.rejectClubRequest(requestId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Club request rejected successfully", null));
    }
}
