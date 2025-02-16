package org.studentclubmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studentclubmanagement.models.*;

import java.util.List;

@Repository
public interface UserClubRepository extends JpaRepository<UserClub, Long> {
    List<UserClub> findByUser_UserId(Long userId);
    boolean existsByUser_UserIdAndClub_ClubId(Long userId, Long clubId);
}