package org.studentclubmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.studentclubmanagement.dtos.ClubDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.exceptions.UserNotFoundException;
import org.studentclubmanagement.models.Club;
import org.studentclubmanagement.models.User;
import org.studentclubmanagement.repositories.ClubRepository;
import org.studentclubmanagement.repositories.UserRepository;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all clubs as a list of ClubDTOs.
     *
     * @return List of ClubDTOs
     */
    public List<ClubDTO> getAllClubs() {
        List<Club> clubs = clubRepository.findAll();
        return clubs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves a club by its ID and converts to ClubDTO with Base64-encoded image.
     *
     * @param id Club ID
     * @return ClubDTO with Base64 image
     * @throws ClubNotFoundException if Club not found
     */
    public ClubDTO getClubByIdWithImage(Long id) throws ClubNotFoundException {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException("Club with ID " + id + " not found"));

        return mapToDTO(club);
    }

    /**
     * Finds a club by its name and returns as ClubDTO with Base64-encoded image.
     *
     * @param clubName Name of the club
     * @return ClubDTO with Base64 image
     * @throws ClubNotFoundException if Club not found
     */
    public ClubDTO findClubByNameWithImage(String clubName) throws ClubNotFoundException {
        Club club = clubRepository.findByClubName(clubName);
        if (club == null) {
            throw new ClubNotFoundException("Club with name " + clubName + " not found");
        }
        return mapToDTO(club);
    }

    /**
     * Creates a new club with an optional image.
     *
     * @param clubDTO Club data transfer object
     * @return Created ClubDTO with Base64 image
     */
    public ClubDTO createClubWithImage(ClubDTO clubDTO) {
        User admin = userRepository.findById(clubDTO.getAdminId())
                .orElseThrow(() -> new UserNotFoundException("Admin not found"));

        Club club = new Club();
        club.setClubName(clubDTO.getClubName());
        club.setDescription(clubDTO.getDescription());
        club.setNoOfMembers(clubDTO.getNoOfMembers());
        club.setAvailableSlots(clubDTO.getAvailableSlots());
        club.setTotalSlots(clubDTO.getTotalSlots());
        club.setClubAdmin(admin);

        // Convert image from MultipartFile to byte[] and store
        if (clubDTO.getImage() != null && !clubDTO.getImage().isEmpty()) {
            try {
                club.setImage(clubDTO.getImage().getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        Club savedClub = clubRepository.save(club);
        return mapToDTO(savedClub);
    }

    /**
     * Updates an existing club with the provided data and optional image.
     *
     * @param id Club ID
     * @param updatedClubDTO Updated ClubDTO data
     * @param image MultipartFile for the image (optional)
     * @return Updated ClubDTO with Base64 image
     * @throws ClubNotFoundException if Club not found
     */
    public ClubDTO updateClubFromDTO(Long id, ClubDTO updatedClubDTO, MultipartFile image) throws ClubNotFoundException {
        User admin = userRepository.findById(updatedClubDTO.getAdminId())
                .orElseThrow(() -> new UserNotFoundException("Admin not found"));
        Club existingClub = clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException("Club with ID " + id + " not found"));

        existingClub.setClubName(updatedClubDTO.getClubName());
        existingClub.setDescription(updatedClubDTO.getDescription());
        existingClub.setNoOfMembers(updatedClubDTO.getNoOfMembers());
        existingClub.setAvailableSlots(updatedClubDTO.getAvailableSlots());
        existingClub.setTotalSlots(updatedClubDTO.getTotalSlots());
        existingClub.setClubAdmin(admin);

        // If new image is provided, update it
        if (image != null && !image.isEmpty()) {
            try {
                existingClub.setImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        Club updatedClub = clubRepository.save(existingClub);
        return mapToDTO(updatedClub);
    }

    /**
     * Deletes a club by its ID.
     *
     * @param id Club ID
     * @throws ClubNotFoundException if Club not found
     */
    public void deleteClub(Long id) throws ClubNotFoundException {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException("Club with ID " + id + " not found"));
        clubRepository.delete(club);
    }

    /**
     * Maps Club entity to ClubDTO including Base64-encoded image.
     *
     * @param club Club entity
     * @return ClubDTO with Base64 image
     */
    private ClubDTO mapToDTO(Club club) {
        ClubDTO clubDTO = new ClubDTO();
        clubDTO.setClubName(club.getClubName());
        clubDTO.setDescription(club.getDescription());
        clubDTO.setNoOfMembers(club.getNoOfMembers());
        clubDTO.setAvailableSlots(club.getAvailableSlots());
        clubDTO.setTotalSlots(club.getTotalSlots());
        clubDTO.setAdminId(club.getClubAdmin().getUserId());
        clubDTO.setClubAdminFirstName(club.getClubAdmin().getFirstName());
        clubDTO.setClubAdminLastName(club.getClubAdmin().getLastName());

        // Convert image to Base64
        if (club.getImage() != null) {
            String base64Image = Base64.getEncoder().encodeToString(club.getImage());
            clubDTO.setImageUrl("data:image/jpeg;base64," + base64Image);
        }
        return clubDTO;
    }
}
