package org.studentclubmanagement.services;

import org.springframework.stereotype.Service;
import org.studentclubmanagement.dtos.AnnouncementDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.exceptions.UserNotFoundException;
import org.studentclubmanagement.exceptions.UnauthorizedActionException;
import org.studentclubmanagement.models.*;
import org.studentclubmanagement.repositories.AnnouncementRepository;
import org.studentclubmanagement.repositories.ClubRepository;
import org.studentclubmanagement.repositories.UserRepository;

import java.util.List;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository, UserRepository userRepository, ClubRepository clubRepository) {
        this.announcementRepository = announcementRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
    }

    /**
     * Create an announcement (Super Admin can post in any club, Club Admin can post in their club only)
     */
    public Announcement createAnnouncement(AnnouncementDTO announcementDTO) throws UnauthorizedActionException, ClubNotFoundException {
        User user = userRepository.findById(announcementDTO.getPostedById())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Club club = clubRepository.findById(announcementDTO.getClubId())
                .orElseThrow(() -> new ClubNotFoundException("Club not found"));

        // Super Admin can post in any club, but Club Admin can only post in their own club
        if (user.getRole() == Role.CLUB_ADMIN && !club.getClubAdmin().equals(user)) {
            throw new UnauthorizedActionException("Club Admin can only post announcements for their club.");
        }
        if (user.getRole() != Role.CLUB_ADMIN && user.getRole() != Role.SUPER_ADMIN) {
            throw new UnauthorizedActionException("Only Club Admins and Super Admins can post announcements.");
        }

        Announcement announcement = new Announcement();
        announcement.setPostedBy(user);
        announcement.setClub(club);
        announcement.setTitle(announcementDTO.getTitle());
        announcement.setContent(announcementDTO.getContent());
        announcement.setType(announcementDTO.getType());

        return announcementRepository.save(announcement);
    }

    /**
     * Get all announcements for a club
     */
    public List<Announcement> getAnnouncementsByClub(Long clubId) {
        return announcementRepository.findByClub_ClubId(clubId);
    }
}
