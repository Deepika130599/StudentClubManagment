package org.studentclubmanagement.controllers;

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
@RequestMapping("/api/announcements")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Announcement APIs", description = "Operations related to announcements or events of clubs.")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    /**
     * Create an Announcement
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Announcement>> createAnnouncement(@RequestBody AnnouncementDTO announcementDTO) throws ClubNotFoundException, UnauthorizedActionException {
        Announcement announcement = announcementService.createAnnouncement(announcementDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("Announcement posted successfully", announcement));
    }

    /**
     * Get Announcements for a Club
     */
    @GetMapping("/{clubId}")
    public ResponseEntity<ApiResponseDTO<List<Announcement>>> getAnnouncementsByClub(@PathVariable Long clubId) {
        List<Announcement> announcements = announcementService.getAnnouncementsByClub(clubId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Announcements fetched successfully", announcements));
    }
}