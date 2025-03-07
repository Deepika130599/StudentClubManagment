package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.studentclubmanagement.dtos.AnnouncementDTO;
import org.studentclubmanagement.dtos.ApiResponseDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.exceptions.UnauthorizedActionException;
import org.studentclubmanagement.models.Announcement;
import org.studentclubmanagement.services.AnnouncementService;

import java.util.List;

@RestController
@RequestMapping("/announcements")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Announcement APIs", description = "APIs for managing club announcements, allowing creation and retrieval of announcements for specific clubs.")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    /**
     * Creates a new announcement for a club.
     *
     * @param announcementDTO The announcement details including club ID, message, and date.
     * @return A response entity containing a success message and the created announcement.
     * @throws ClubNotFoundException If the specified club does not exist.
     * @throws UnauthorizedActionException If the user is not authorized to create an announcement.
     */
    @PostMapping
    @Operation(
        summary = "Create an Announcement",
        description = "Allows club admins to create an announcement for their respective clubs."
    )
    public ResponseEntity<ApiResponseDTO<Announcement>> createAnnouncement(@RequestBody AnnouncementDTO announcementDTO)
            throws ClubNotFoundException, UnauthorizedActionException {
        Announcement announcement = announcementService.createAnnouncement(announcementDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("Announcement posted successfully", announcement));
    }

    /**
     * Retrieves all announcements for a specific club.
     *
     * @param clubId The unique ID of the club whose announcements need to be fetched.
     * @return A response entity containing a list of announcements for the specified club.
     */
    @GetMapping("/{clubId}")
    @Operation(
        summary = "Get Announcements for a Club",
        description = "Fetches all announcements associated with a specific club ID."
    )
    public ResponseEntity<ApiResponseDTO<List<Announcement>>> getAnnouncementsByClub(
        @Parameter(
            description = "The unique ID of the club whose announcements need to be retrieved",
            required = true,
            example = "1"
        )
        @PathVariable Long clubId
    ) {
        List<Announcement> announcements = announcementService.getAnnouncementsByClub(clubId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Announcements fetched successfully", announcements));
    }
}
