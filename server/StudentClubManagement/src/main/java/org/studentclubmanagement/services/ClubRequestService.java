package org.studentclubmanagement.services;

import org.springframework.stereotype.Service;
import org.studentclubmanagement.dtos.ClubRequestDTO;
import org.studentclubmanagement.exceptions.*;
import org.studentclubmanagement.models.*;
import org.studentclubmanagement.repositories.ClubRequestRepository;
import org.studentclubmanagement.repositories.ClubRepository;
import org.studentclubmanagement.repositories.UserRepository;

import java.util.List;

@Service
public class ClubRequestService {

    private final ClubRequestRepository clubRequestRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final UserClubService userClubService;

    public ClubRequestService(ClubRequestRepository clubRequestRepository, UserRepository userRepository, ClubRepository clubRepository, UserClubService userClubService) {
        this.clubRequestRepository = clubRequestRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.userClubService = userClubService;
    }

    /**
     *  Create a new Club Request
     */
    public ClubRequest createClubRequest(ClubRequestDTO clubRequestDTO) throws ClubLimitExceededException, RequestAlreadyExistsException, ClubNotFoundException {
        User user = userRepository.findById(clubRequestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Club club = clubRepository.findById(clubRequestDTO.getClubId())
                .orElseThrow(() -> new ClubNotFoundException("Club not found"));

        // Ensure user has not exceeded club limit (Max 3 clubs)
        if (user.getJoinedClubs() >= 3) {
            throw new ClubLimitExceededException("User has already joined 3 clubs.");
        }

        // Ensure user has not already requested to join this club
        if (clubRequestRepository.existsByUserAndClub(user, club)) {
            throw new RequestAlreadyExistsException("User has already requested to join this club.");
        }

        ClubRequest clubRequest = new ClubRequest();
        clubRequest.setUser(user);
        clubRequest.setClub(club);
        clubRequest.setComment(clubRequestDTO.getComment());
        clubRequest.setStatus(RequestStatus.PENDING);

        return clubRequestRepository.save(clubRequest);
    }

    /**
     * Get all pending club requests for a club (Admin view)
     */
    public List<ClubRequest> getClubRequestsByClubId(Long clubId) {
        return clubRequestRepository.findByClub_ClubIdAndStatus(clubId, RequestStatus.PENDING);
    }

    /**
     * Approve a Club Request
     */
    public void approveClubRequest(Long requestId) throws ClubLimitExceededException, ClubCapacityExceededException, RequestAlreadyExistsException {
        ClubRequest clubRequest = clubRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Club request not found"));

        // Ensure request is still pending
        if (!clubRequest.getStatus().equals(RequestStatus.PENDING)) {
            throw new RuntimeException("Club request is already processed.");
        }

        // Delegate full approval process to UserClubService
        userClubService.approveClubRequest(requestId);
    }

    /**
     * Reject a Club Request
     */
    public void rejectClubRequest(Long requestId) {
        ClubRequest clubRequest = clubRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Club request not found"));

        // Ensure request is still pending
        if (!clubRequest.getStatus().equals(RequestStatus.PENDING)) {
            throw new RuntimeException("Club request is already processed.");
        }

        clubRequest.setStatus(RequestStatus.REJECTED);
        clubRequestRepository.save(clubRequest);
    }
}
