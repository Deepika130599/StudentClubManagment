package org.studentclubmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.studentclubmanagement.dtos.SignInRequestDTO;
import org.studentclubmanagement.dtos.UpdateUserDTO;
import org.studentclubmanagement.dtos.UserDTO;
import org.studentclubmanagement.models.User;
import org.studentclubmanagement.repositories.UserRepository;
import org.studentclubmanagement.exceptions.*;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }

    public User createUserFromDTO(UserDTO userDTO) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new UserAlreadyExistsException("User with email " + userDTO.getEmail() + " already exists");
        }
        return assignUser(userDTO);
    }

    public User updateUserFromDTO(Long id, UpdateUserDTO updatedUserDTO) throws UserNotFoundException {
        User existingUser = getUserById(id);
        return updateUser(updatedUserDTO, existingUser);
    }

    private User updateUser(UpdateUserDTO userDTO, User existingUser) throws UserAlreadyExistsException {
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setAddress(userDTO.getAddress());

        return userRepository.save(existingUser);
    }
    private User assignUser(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Hash the password before saving it to the database
        user.setAddress(userDTO.getAddress());
        user.setRole(userDTO.getRole());
        user.setBirthday(userDTO.getBirthday());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) throws UserNotFoundException {
        User existingUser = getUserById(id);
        userRepository.delete(existingUser);
    }

    public String authenticateUser(SignInRequestDTO signInRequestDTO) throws UserNotFoundException{
        try{
            User user = userRepository.findByEmail(signInRequestDTO.getEmail());
            boolean flag = validatePassword(signInRequestDTO.getPassword(), user.getPassword());
            return flag ? "Login Successful" : "Incorrect Password! Please try again";
        } catch (Exception e) {
            throw new UserNotFoundException("Incorrect Email Address");
        }
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    public User updateUserFromUserDTO(Long id, UserDTO userDTO) {
        User existingUser = getUserById(id);
        return updateUserByAdmin(userDTO, existingUser);
    }

    private User updateUserByAdmin(UserDTO userDTO, User existingUser) throws UserNotFoundException {
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Hash the password before saving it to the database
        existingUser.setAddress(userDTO.getAddress());
        existingUser.setRole(userDTO.getRole());
        existingUser.setBirthday(userDTO.getBirthday());

        return userRepository.save(existingUser);
    }

    public List<User> getAllUsersStartingWithEmail(String email) {
        return userRepository.findByEmailStartingWith(email);
    }

    public List<User> getAllUsersStartingWithName(String name) {
        return userRepository.findByFirstNameAndLastNameStartingWith(name, name);
    }
}