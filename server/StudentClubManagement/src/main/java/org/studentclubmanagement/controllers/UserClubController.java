package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.studentclubmanagement.dtos.ApiResponseDTO;
import org.studentclubmanagement.exceptions.ClubCapacityExceededException;
import org.studentclubmanagement.exceptions.ClubLimitExceededException;
import org.studentclubmanagement.exceptions.RequestAlreadyExistsException;
import org.studentclubmanagement.models.UserClub;
import org.studentclubmanagement.services.UserClubService;

import java.util.List;

@RestController
@RequestMapping("/user-clubs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "User Club APIs", description = "APIs for managing user club memberships, including approvals, rejections, and fetching joined clubs.")
public class UserClubController {

    private final UserClubService userClubService;

    public UserClubController(UserClubService userClubService) {
        this.userClubService = userClubService;
    }

    /**
     * Approves a club request, allowing a user to join a club.
     *
     * @param requestId The unique ID of the club request to approve.
     * @return A response entity confirming the approval of the club request.
     * @throws ClubLimitExceededException If the user has already joined the maximum number of clubs.
     * @throws ClubCapacityExceededException If the club has reached its maximum capacity.
     * @throws RequestAlreadyExistsException If the user is already a member of the club.
     */
    @PutMapping("/{requestId}/approve")
    @Operation(
        summary = "Approve a Club Request",
        description = "Allows an admin to approve a club join request, adding the user to the club."
    )
    public ResponseEntity<ApiResponseDTO> approveClubRequest(
        @Parameter(description = "The unique ID of the club request to approve", required = true, example = "5")
        @PathVariable Long requestId
    ) throws ClubLimitExceededException, ClubCapacityExceededException, RequestAlreadyExistsException {
        userClubService.approveClubRequest(requestId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Club request approved successfully", null));
    }

    /**
     * Rejects a club request, preventing a user from joining the club.
     *
     * @param requestId The unique ID of the club request to reject.
     * @return A response entity confirming the rejection of the club request.
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
        userClubService.rejectClubRequest(requestId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Club request rejected successfully", null));
    }

    /**
     * Retrieves all clubs that a user has joined.
     *
     * @param userId The unique ID of the user whose joined clubs need to be fetched.
     * @return A response entity containing a list of clubs the user is a member of.
     */
    @GetMapping("/{userId}")
    @Operation(
        summary = "Get Clubs Joined by User",
        description = "Fetches all clubs that a specific user has joined."
    )
    public ResponseEntity<ApiResponseDTO<List<UserClub>>> getUserClubs(
        @Parameter(description = "The unique ID of the user whose clubs need to be retrieved", required = true, example = "10")
        @PathVariable Long userId
    ) {
        List<UserClub> userClubs = userClubService.getUserClubs(userId);
        return ResponseEntity.ok(new ApiResponseDTO<>("User clubs fetched successfully", userClubs));
    }
}
