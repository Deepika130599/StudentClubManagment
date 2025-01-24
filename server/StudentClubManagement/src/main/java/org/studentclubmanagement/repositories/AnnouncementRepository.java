package org.studentclubmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studentclubmanagement.models.*;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    // Custom query methods if needed
}