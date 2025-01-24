package org.studentclubmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.studentclubmanagement.dtos.UserDTO;
import org.studentclubmanagement.models.User;
import org.studentclubmanagement.repositories.UserRepository;
import org.studentclubmanagement.exceptions.*;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setRole(userDTO.getRole());
        user.setBirthday(userDTO.getBirthday());

        return userRepository.save(user);
    }

    public User updateUserFromDTO(Long id, UserDTO updatedUserDTO) throws UserNotFoundException {
        User existingUser = getUserById(id);

        existingUser.setFirstName(updatedUserDTO.getFirstName());
        existingUser.setLastName(updatedUserDTO.getLastName());
        existingUser.setEmail(updatedUserDTO.getEmail());
        existingUser.setPassword(updatedUserDTO.getPassword());
        existingUser.setPhone(updatedUserDTO.getPhone());
        existingUser.setAddress(updatedUserDTO.getAddress());
        existingUser.setRole(updatedUserDTO.getRole());
        existingUser.setBirthday(updatedUserDTO.getBirthday());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) throws UserNotFoundException {
        User existingUser = getUserById(id);
        userRepository.delete(existingUser);
    }
}