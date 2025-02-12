package org.studentclubmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.studentclubmanagement.dtos.ClubDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.exceptions.UserNotFoundException;
import org.studentclubmanagement.models.Club;
import org.studentclubmanagement.models.User;
import org.studentclubmanagement.repositories.ClubRepository;
import org.studentclubmanagement.repositories.UserRepository;

import java.util.List;

@Service
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Club getClubById(Long id) throws ClubNotFoundException {
        return clubRepository.findById(id)
                .orElseThrow(() -> new ClubNotFoundException("Club with ID " + id + " not found"));
    }

    public Club findClubByName(String clubName) throws ClubNotFoundException {
        if(!clubName.isBlank()){
            try {
                return clubRepository.findByClubName(clubName);
            }
            catch(Exception e){
                throw new ClubNotFoundException("Club with name " + clubName + " not found");
            }
        }
        return null;
    }

    public Club createClub(ClubDTO clubDTO) {
        User admin = userRepository.findById(clubDTO.getAdminId())
                .orElseThrow(() -> new UserNotFoundException("Admin not found"));
        Club club = new Club();
        return getClub(admin, clubDTO, club);
    }

    public Club updateClubFromDTO(Long id, ClubDTO updatedClubDTO) throws ClubNotFoundException {
        User admin = userRepository.findById(updatedClubDTO.getAdminId())
                .orElseThrow(() -> new UserNotFoundException("Admin not found"));
        Club existingClub = getClubById(id);
        return getClub(admin, updatedClubDTO, existingClub);

    }

    private Club getClub(User admin, ClubDTO clubDTO, Club club) {
        club.setClubName(clubDTO.getClubName());
        club.setDescription(clubDTO.getDescription());
        club.setNoOfMembers(clubDTO.getNoOfMembers());
        club.setAvailableSlots(clubDTO.getAvailableSlots());
        club.setTotalSlots(clubDTO.getTotalSlots());
        club.setClubAdmin(admin);
        return clubRepository.save(club);
    }

    public void deleteClub(Long id) throws ClubNotFoundException {
        Club club = getClubById(id);
        clubRepository.delete(club);
    }
}
