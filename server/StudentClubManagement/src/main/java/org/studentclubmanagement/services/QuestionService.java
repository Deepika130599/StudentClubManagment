package org.studentclubmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.studentclubmanagement.dtos.QuestionRequestDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.exceptions.UndefinedUserClubException;
import org.studentclubmanagement.exceptions.UserNotFoundException;
import org.studentclubmanagement.models.Club;
import org.studentclubmanagement.models.Question;
import org.studentclubmanagement.models.User;
import org.studentclubmanagement.repositories.ClubRepository;
import org.studentclubmanagement.repositories.QuestionRepository;
import org.studentclubmanagement.repositories.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;


    public Question createQuestion(QuestionRequestDTO questionRequestDTO) throws ClubNotFoundException, UndefinedUserClubException {
        User user = userRepository.findById(questionRequestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Club club = clubRepository.findById(questionRequestDTO.getClubId())
                .orElseThrow(() -> new ClubNotFoundException("Club not found"));

        AtomicBoolean memberOfTheClub = new AtomicBoolean(false);
        user.getUserClubs().forEach(clubUser -> {
            if(Objects.equals(club.getClubId(), questionRequestDTO.getClubId())) {
                memberOfTheClub.set(true);
            }
        });
        if(!memberOfTheClub.get()) {
            throw new UndefinedUserClubException("User Doesn't belong to the club");
        }
        Question question = new Question();
        question.setTitle(questionRequestDTO.getTitle());
        question.setQuestion(questionRequestDTO.getQuestion());
        question.setClub(club);
        question.setUser(user);
        return questionRepository.save(question);
    }

    /**
     * Get all questions for a club
     */
    public List<Question> getQuestionsByClub(int clubId) {
        return questionRepository.findByClub_ClubId(clubId);
    }
}
