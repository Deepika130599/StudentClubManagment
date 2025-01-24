package org.studentclubmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studentclubmanagement.models.*;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    // Custom query methods if needed
    Club findByClubName(String clubName);
}