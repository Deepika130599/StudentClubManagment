package org.studentclubmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studentclubmanagement.models.*;

import java.util.List;

@Repository
public interface ClubRequestRepository extends JpaRepository<ClubRequest, Long> {
    List<ClubRequest> findByClub_ClubIdAndStatus(Long clubId, RequestStatus status);
    boolean existsByUserAndClub(User user, Club club);

    List<ClubRequest> findByUser_UserId(Long userId);
}