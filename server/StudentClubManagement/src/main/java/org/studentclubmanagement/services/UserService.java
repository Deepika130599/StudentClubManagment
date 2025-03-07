package org.studentclubmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.studentclubmanagement.dtos.SignInRequestDTO;
import org.studentclubmanagement.dtos.UpdateUserDTO;
import org.studentclubmanagement.dtos.UserDTO;
import org.studentclubmanagement.dtos.UserResponseDTO;
import org.studentclubmanagement.models.User;
import org.studentclubmanagement.repositories.UserRepository;
import org.studentclubmanagement.exceptions.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::getUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserInUserResponseDTO(Long id) {
        User user = getUserById(id);
        return getUserResponse(user);
    }

    public User getUserById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
    }

    private UserResponseDTO getUserResponse(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUserId(user.getUserId());
        userResponseDTO.setFirstName(user.getFirstName());
        userResponseDTO.setLastName(user.getLastName());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setPhone(user.getPhone());
        userResponseDTO.setAddress(user.getAddress());
        userResponseDTO.setBirthday(user.getBirthday());
        userResponseDTO.setJoinedClubs(user.getJoinedClubs());
        userResponseDTO.setCreatedAt(user.getCreatedAt());
        userResponseDTO.setUpdatedAt(user.getUpdatedAt());
        userResponseDTO.setUserClubs(user.getUserClubs());
        return userResponseDTO;
    }

    public User createUserFromDTO(UserDTO userDTO) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new UserAlreadyExistsException("User with email " + userDTO.getEmail() + " already exists");
        }
        return saveUser(userDTO);
    }

    public UserResponseDTO updateUserFromDTO(Long id, UpdateUserDTO updatedUserDTO) throws UserNotFoundException {
        User existingUser = getUserById(id);
        User updatedUser = updateUser(updatedUserDTO, existingUser);
        return getUserResponse(updatedUser);
    }

    private User updateUser(UpdateUserDTO userDTO, User existingUser) {
        existingUser.setFirstName(userDTO.getFirstName());
        existingUser.setLastName(userDTO.getLastName());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setAddress(userDTO.getAddress());
        return userRepository.save(existingUser);
    }

    private User saveUser(UserDTO userDTO) {
        User user = new User();
        User savedUser = getUser(userDTO, user);
        user.setUserId(savedUser.getUserId());
        return user;

    }

    private User getUser(UserDTO userDTO, User user) {
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

    public UserResponseDTO updateUserFromUserDTO(Long id, UserDTO userDTO) {
        User existingUser = getUserById(id);
        User updatedUser = updateUserByAdmin(userDTO, existingUser);
        return getUserResponse(updatedUser);
    }

    private User updateUserByAdmin(UserDTO userDTO, User existingUser) {
        return getUser(userDTO, existingUser);
    }

    public List<UserResponseDTO> getAllUsersStartingWithEmail(String email) {
        return userRepository.findByEmailStartingWith(email)
                .stream()
                .map(this::getUserResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponseDTO> getAllUsersStartingWithName(String name) {
        return userRepository.findByFirstNameAndLastNameStartingWith(name, name)
                .stream()
                .map(this::getUserResponse)
                .collect(Collectors.toList());
    }

    /**
     * Load user by email for authentication (Spring Security)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Convert role string into a list of SimpleGrantedAuthority
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }


    /**
     * Fetch user role by email
     */
    public String getUserRole(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User role not found for email: " + email);
        }
        return "ROLE_" + user.getRole();
    }
}
