package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.studentclubmanagement.dtos.ApiResponseDTO;
import org.studentclubmanagement.dtos.UserDTO;
import org.studentclubmanagement.models.User;
import org.studentclubmanagement.services.UserService;
import org.studentclubmanagement.exceptions.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "User APIs", description = "Operations related to users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponseDTO("Successfully retrieved all users", users));
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(new ApiResponseDTO("User retrieved successfully", user));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO("User not found", null));
        }
    }

    /**
     *
     * @param userDTO
     * @return
     */
    @PostMapping
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
     *
     * @param id
     * @param updatedUserDTO
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateUser(@PathVariable Long id, @Validated @RequestBody UserDTO updatedUserDTO) {
        try {
            User savedUser = userService.updateUserFromDTO(id, updatedUserDTO);
            return ResponseEntity.ok(new ApiResponseDTO<>("User updated successfully", savedUser));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("Error: User with id " + id + " not found", null));
        }
    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponseDTO("User with ID "+id+" has been successfully deleted.", null));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("ERROR: User with ID "+id+" not found", null));
        }
    }
}
