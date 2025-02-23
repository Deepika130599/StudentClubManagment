package org.studentclubmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.studentclubmanagement.models.*;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findByTitle(String title);

    List<Question> findByClub_ClubId(int clubId);
}
