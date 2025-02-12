package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.studentclubmanagement.dtos.ClubDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.exceptions.UserNotFoundException;
import org.studentclubmanagement.models.Club;
import org.studentclubmanagement.models.User;
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
     *
     * @return List of Clubs.
     */
    @GetMapping
    public ResponseEntity<List<Club>> getAllClubs() {
        List<Club> clubs = clubService.getAllClubs();
        return ResponseEntity.ok(clubs);
    }

    /**
     *
     * @param id
     * @return Club by clubId
     */
    @GetMapping("/{id}")
    public ResponseEntity<Club> getClubById(@PathVariable Long id) {
        try {
            Club club = clubService.getClubById(id);
            return ResponseEntity.ok(club);
        } catch (ClubNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/getClubByName")
    public ResponseEntity<Club> getClubByName(@RequestParam String clubName) {
        try {
            Club club = clubService.findClubByName(clubName);
            return ResponseEntity.ok(club);
        } catch (ClubNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     *
     * Creates a new club in the club entity.
     * @param clubDTO
     * @return Club
     */
    @PostMapping
    public ResponseEntity<Club> createClub(@Valid @RequestBody ClubDTO clubDTO) {
        Club savedClub = clubService.createClub(clubDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClub);
    }

}
