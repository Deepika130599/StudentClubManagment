package org.studentclubmanagement.controllers;

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
@RequestMapping("/api/user-clubs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "User Club APIs", description = "Operations related to users and their clubs.")
public class UserClubController {

    private final UserClubService userClubService;

    public UserClubController(UserClubService userClubService) {
        this.userClubService = userClubService;
    }

    /**
     * Approve a Club Request (Move to UserClub)
     */
    @PutMapping("/{requestId}/approve")
    public ResponseEntity<ApiResponseDTO> approveClubRequest(@PathVariable Long requestId) throws ClubLimitExceededException, ClubCapacityExceededException, RequestAlreadyExistsException {
        userClubService.approveClubRequest(requestId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Club request approved successfully", null));
    }

    /**
     * Reject a Club Request
     */
    @PutMapping("/{requestId}/reject")
    public ResponseEntity<ApiResponseDTO> rejectClubRequest(@PathVariable Long requestId) {
        userClubService.rejectClubRequest(requestId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Club request rejected successfully", null));
    }

    /**
     * Get All Clubs a User Has Joined
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponseDTO<List<UserClub>>> getUserClubs(@PathVariable Long userId) {
        List<UserClub> userClubs = userClubService.getUserClubs(userId);
        return ResponseEntity.ok(new ApiResponseDTO<>("User clubs fetched successfully", userClubs));
    }
}

