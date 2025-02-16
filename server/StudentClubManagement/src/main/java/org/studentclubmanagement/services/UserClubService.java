package org.studentclubmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.studentclubmanagement.exceptions.*;
import org.studentclubmanagement.models.*;
import org.studentclubmanagement.repositories.*;

import java.util.List;

@Service
public class UserClubService {

    private final UserClubRepository userClubRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubRequestRepository clubRequestRepository;

    public UserClubService(UserClubRepository userClubRepository, UserRepository userRepository,
                           ClubRepository clubRepository, ClubRequestRepository clubRequestRepository) {
        this.userClubRepository = userClubRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.clubRequestRepository = clubRequestRepository;
    }

    /**
     * Approve a Club Request and Add User to UserClub
     */
    @Transactional
    public void approveClubRequest(Long requestId) throws ClubCapacityExceededException, ClubLimitExceededException, RequestAlreadyExistsException {
        ClubRequest clubRequest = clubRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Club request not found"));

        User user = clubRequest.getUser();
        Club club = clubRequest.getClub();

        // Check constraints before approving
        if (user.getJoinedClubs() >= 3) {
            throw new ClubLimitExceededException("User has already joined 3 clubs.");
        }
        if (club.getAvailableSlots() <= 0) {
            throw new ClubCapacityExceededException("Club is full, no available slots.");
        }
        if (userClubRepository.existsByUser_UserIdAndClub_ClubId(user.getUserId(), club.getClubId())) {
            throw new RequestAlreadyExistsException("User is already a member of this club.");
        }

        // Create new UserClub entry
        UserClub userClub = new UserClub();
        userClub.setUser(user);
        userClub.setClub(club);
        userClub.setComment(clubRequest.getComment());
        userClubRepository.save(userClub);

        // Update User & Club Details
        user.setJoinedClubs(user.getJoinedClubs() + 1);
        club.setNoOfMembers(club.getNoOfMembers() + 1);
        club.setAvailableSlots(club.getAvailableSlots() - 1);

        userRepository.save(user);
        clubRepository.save(club);

        // Update Request Status
        clubRequest.setStatus(RequestStatus.APPROVED);
        clubRequestRepository.save(clubRequest);
    }


    /**
     * Reject a Club Request
     */
    public void rejectClubRequest(Long requestId) {
        ClubRequest clubRequest = clubRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Club request not found"));

        clubRequest.setStatus(RequestStatus.REJECTED);
        clubRequestRepository.save(clubRequest);
    }

    /**
     * Get All Clubs a User Has Joined
     */
    public List<UserClub> getUserClubs(Long userId) {
        return userClubRepository.findByUser_UserId(userId);
    }
}
