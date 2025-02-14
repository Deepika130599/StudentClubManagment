package org.studentclubmanagement.repositories;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.studentclubmanagement.models.*;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findByEmailStartingWith(@Email(message = "Email should be valid") @NotBlank(message = "Email is mandatory") String email);

    @Query("SELECT u FROM User u WHERE u.firstName LIKE CONCAT(:firstName, '%') OR u.lastName LIKE CONCAT(:lastName, '%')")
    List<User> findByFirstNameAndLastNameStartingWith(@Param("firstName") String firstName, @Param("lastName") String lastName);

}