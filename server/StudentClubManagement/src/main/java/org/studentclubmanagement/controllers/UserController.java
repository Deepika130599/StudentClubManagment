package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.studentclubmanagement.dtos.ApiResponseDTO;
import org.studentclubmanagement.dtos.SignInRequestDTO;
import org.studentclubmanagement.dtos.UpdateUserDTO;
import org.studentclubmanagement.dtos.UserDTO;
import org.studentclubmanagement.models.User;
import org.studentclubmanagement.services.UserService;
import org.studentclubmanagement.exceptions.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
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

    @GetMapping("/email")
    public ResponseEntity<ApiResponseDTO> getUserByEmail(@RequestParam String email) {
        List<User> users = userService.getAllUsersStartingWithEmail(email);
        return ResponseEntity.ok(new ApiResponseDTO("Successfully retrieved all users", users));
    }

    @GetMapping("/name")
    public ResponseEntity<ApiResponseDTO> getUserByName(@RequestParam String name) {
        List<User> users = userService.getAllUsersStartingWithName(name);
        return ResponseEntity.ok(new ApiResponseDTO("Successfully retrieved all users", users));
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
    public ResponseEntity<ApiResponseDTO> updateUser(@PathVariable Long id, @Validated @RequestBody UpdateUserDTO updatedUserDTO) {
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
     * @param userDTO
     * @return
     */
    @PutMapping("/updateByAdmin/{id}")
    public ResponseEntity<ApiResponseDTO> updateUserByAdmin(@PathVariable Long id, @Validated @RequestBody UserDTO userDTO) {
        try {
            User savedUser = userService.updateUserFromUserDTO(id, userDTO);
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


    /**
     *
     * @param signInRequestDTO
     * @return
     * @throws UserNotFoundException
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO> login(@Validated @RequestBody SignInRequestDTO signInRequestDTO) throws UserNotFoundException {
        try{
            String message = userService.authenticateUser(signInRequestDTO);
            boolean data = message.equals("Login Successful") ? true : false;
            return ResponseEntity.ok(new ApiResponseDTO(message, data));
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDTO<>("ERROR: User with Email "+signInRequestDTO.getEmail()+" not found", false));
        }
    }
}
