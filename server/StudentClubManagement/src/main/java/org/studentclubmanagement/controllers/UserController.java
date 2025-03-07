package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.studentclubmanagement.dtos.*;
import org.studentclubmanagement.models.User;
import org.studentclubmanagement.services.UserService;
import org.studentclubmanagement.exceptions.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "User APIs", description = "APIs for managing user information, including retrieval, creation, updating, and deletion of users.")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves all users in the system.
     *
     * @return A list of all users wrapped in an ApiResponseDTO.
     */
    @GetMapping
    @Operation(
        summary = "Get All Users",
        description = "Fetches a list of all registered users."
    )
    public ResponseEntity<ApiResponseDTO> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponseDTO("Successfully retrieved all users", users));
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id The unique ID of the user.
     * @return The user's details if found.
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Get User by ID",
        description = "Fetches user details by their unique ID."
    )
    public ResponseEntity<ApiResponseDTO> getUserById(
        @Parameter(description = "The unique ID of the user", required = true, example = "1")
        @PathVariable Long id
    ) {
        try {
            UserResponseDTO user = userService.getUserInUserResponseDTO(id);
            return ResponseEntity.ok(new ApiResponseDTO("User retrieved successfully", user));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("User not found", null));
        }
    }

    /**
     * Retrieves users whose email starts with the given email prefix.
     *
     * @param email The email prefix to search for.
     * @return A list of users matching the email prefix.
     */
    @GetMapping("/email")
    @Operation(
        summary = "Get Users by Email Prefix",
        description = "Fetches users whose email starts with the given prefix."
    )
    public ResponseEntity<ApiResponseDTO> getUserByEmail(
        @Parameter(description = "The starting characters of the user's email", required = true, example = "john")
        @RequestParam String email
    ) {
        List<UserResponseDTO> users = userService.getAllUsersStartingWithEmail(email);
        return ResponseEntity.ok(new ApiResponseDTO("Successfully retrieved all users", users));
    }

    /**
     * Retrieves users whose first or last name starts with the given name prefix.
     *
     * @param name The name prefix to search for.
     * @return A list of users matching the name prefix.
     */
    @GetMapping("/name")
    @Operation(
        summary = "Get Users by Name Prefix",
        description = "Fetches users whose first or last name starts with the given prefix."
    )
    public ResponseEntity<ApiResponseDTO> getUserByName(
        @Parameter(description = "The starting characters of the user's first or last name", required = true, example = "John")
        @RequestParam String name
    ) {
        List<UserResponseDTO> users = userService.getAllUsersStartingWithName(name);
        return ResponseEntity.ok(new ApiResponseDTO("Successfully retrieved all users", users));
    }

    /**
     * Creates a new user.
     *
     * @param userDTO The details of the user to be created.
     * @return The newly created user's details.
     */
    @PostMapping
    @Operation(
        summary = "Create a New User",
        description = "Allows an admin to create a new user with the provided details."
    )
    public ResponseEntity<ApiResponseDTO> createUser(@Validated @RequestBody UserDTO userDTO) {
        try {
            User savedUser = userService.createUserFromDTO(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO("User Created Successfully", savedUser));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponseDTO("User Already Exists [CHECK EMAIL & USER ID]", null));
        }
    }

    /**
     * Updates an existing user's details.
     *
     * @param id The unique ID of the user.
     * @param updatedUserDTO The updated user details.
     * @return The updated user details if successful.
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Update User Information",
        description = "Updates the details of an existing user."
    )
    public ResponseEntity<ApiResponseDTO> updateUser(
        @Parameter(description = "The unique ID of the user", required = true, example = "1")
        @PathVariable Long id,
        @Validated @RequestBody UpdateUserDTO updatedUserDTO
    ) {
        try {
            UserResponseDTO savedUser = userService.updateUserFromDTO(id, updatedUserDTO);
            return ResponseEntity.ok(new ApiResponseDTO<>("User updated successfully", savedUser));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("Error: User with id " + id + " not found", null));
        }
    }

    /**
     * Allows an admin to update a user's details.
     *
     * @param id The unique ID of the user.
     * @param userDTO The updated user details.
     * @return The updated user details if successful.
     */
    @PutMapping("/updateByAdmin/{id}")
    @Operation(
        summary = "Admin: Update User Information",
        description = "Allows an admin to update an existing user's details."
    )
    public ResponseEntity<ApiResponseDTO> updateUserByAdmin(
        @Parameter(description = "The unique ID of the user", required = true, example = "1")
        @PathVariable Long id,
        @Validated @RequestBody UserDTO userDTO
    ) {
        try {
            UserResponseDTO savedUser = userService.updateUserFromUserDTO(id, userDTO);
            return ResponseEntity.ok(new ApiResponseDTO<>("User updated successfully", savedUser));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("Error: User with id " + id + " not found", null));
        }
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param id The unique ID of the user to delete.
     * @return A success or error message.
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a User",
        description = "Deletes a user from the system by their unique ID."
    )
    public ResponseEntity<ApiResponseDTO> deleteUser(
        @Parameter(description = "The unique ID of the user", required = true, example = "1")
        @PathVariable Long id
    ) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponseDTO("User with ID " + id + " has been successfully deleted.", null));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("ERROR: User with ID " + id + " not found", null));
        }
    }
}
