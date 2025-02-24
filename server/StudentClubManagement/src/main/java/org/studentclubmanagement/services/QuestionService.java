package org.studentclubmanagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.studentclubmanagement.dtos.AnswerResponseDTO;
import org.studentclubmanagement.dtos.QuestionRequestDTO;
import org.studentclubmanagement.dtos.QuestionResponseDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.exceptions.UndefinedUserClubException;
import org.studentclubmanagement.exceptions.UserNotFoundException;
import org.studentclubmanagement.models.Answer;
import org.studentclubmanagement.models.Club;
import org.studentclubmanagement.models.Question;
import org.studentclubmanagement.models.User;
import org.studentclubmanagement.repositories.AnswerRepository;
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
    @Autowired
    private AnswerRepository answerRepository;


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

    public List<QuestionResponseDTO> getQuestionsByClub(int clubId) {
        // Fetch all questions by Club ID
        List<Question> questions = questionRepository.findByClub_ClubId(clubId);

        // Extract question IDs
        List<Long> questionIds = questions.stream()
                .map(Question::getQuestionId)
                .toList();

        // Fetch all answers for the extracted question IDs
        List<Answer> answers = answerRepository.findByQuestion_QuestionIdIn(questionIds);

        // Map answers to AnswerResponseDTO
        List<AnswerResponseDTO> answerDTOs = answers.stream().map(answer -> {
            AnswerResponseDTO answerDTO = new AnswerResponseDTO();
            answerDTO.setAnswerId(answer.getAnswerId());
            answerDTO.setQuestionId(answer.getQuestion().getQuestionId());
            answerDTO.setUserId(answer.getUser().getUserId());
            answerDTO.setFirstName(answer.getUser().getFirstName());
            answerDTO.setLastName(answer.getUser().getLastName());
            answerDTO.setRole(answer.getUser().getRole());
            answerDTO.setClubId(answer.getClub().getClubId());
            answerDTO.setAnswer(answer.getAnswer());
            answerDTO.setCreatedAt(answer.getCreatedAt());
            answerDTO.setUpdatedAt(answer.getUpdatedAt());
            return answerDTO;
        }).toList();

        // Map answers to their respective questions
        List<QuestionResponseDTO> responseDTOs = questions.stream().map(question -> {
            List<AnswerResponseDTO> questionAnswers = answerDTOs.stream()
                    .filter(answerDTO -> answerDTO.getQuestionId().equals(question.getQuestionId()))
                    .toList();

            QuestionResponseDTO dto = new QuestionResponseDTO();
            dto.setQuestionId(question.getQuestionId());
            dto.setClubId(question.getClub().getClubId());
            setUser(dto, question.getUser());
            dto.setUpvoteCount(question.getUpvoteCount());
            dto.setTitle(question.getTitle());
            dto.setQuestion(question.getQuestion());
            dto.setCreatedAt(question.getCreatedAt());
            dto.setUpdatedAt(question.getUpdatedAt());
            dto.setAnswers(questionAnswers);

            return dto;
        }).toList();

        return responseDTOs;
    }


    private void setUser(QuestionResponseDTO dto, User user) {
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
    }
}
