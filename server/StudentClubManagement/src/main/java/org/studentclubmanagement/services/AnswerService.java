package org.studentclubmanagement.services;

import org.springframework.stereotype.Service;
import org.studentclubmanagement.dtos.AnswerDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.exceptions.QuestionNotFoundException;
import org.studentclubmanagement.exceptions.UserNotFoundException;
import org.studentclubmanagement.models.*;
import org.studentclubmanagement.repositories.AnswerRepository;
import org.studentclubmanagement.repositories.ClubRepository;
import org.studentclubmanagement.repositories.QuestionRepository;
import org.studentclubmanagement.repositories.UserRepository;

import java.util.List;

@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final QuestionRepository questionRepository;

    public AnswerService(AnswerRepository answerRepository, UserRepository userRepository,
                         ClubRepository clubRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.questionRepository = questionRepository;
    }

    /**
     * Create a new answer
     */
    public Answer createAnswer(AnswerDTO answerDTO) throws ClubNotFoundException, QuestionNotFoundException {
        User user = userRepository.findById(answerDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Club club = clubRepository.findById(answerDTO.getClubId())
                .orElseThrow(() -> new ClubNotFoundException("Club not found"));

        Question question = questionRepository.findById(answerDTO.getQuestionId())
                .orElseThrow(() -> new QuestionNotFoundException("Question not found"));

        Answer answer = new Answer();
        answer.setUser(user);
        answer.setClub(club);
        answer.setQuestion(question);
        answer.setAnswer(answerDTO.getAnswer());

        return answerRepository.save(answer);
    }

    /**
     * Get all answers for a question
     */
    public List<Answer> getAnswersByQuestion(Long questionId) {
        return answerRepository.findByQuestion_QuestionId(questionId);
    }
}
