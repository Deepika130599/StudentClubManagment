package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.studentclubmanagement.dtos.AnswerDTO;
import org.studentclubmanagement.dtos.ApiResponseDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.exceptions.QuestionNotFoundException;
import org.studentclubmanagement.models.Answer;
import org.studentclubmanagement.services.AnswerService;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Answer APIs", description = "Operations related to answers to questions in a club.")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    /**
     * Create an Answer
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO> createAnswer(@RequestBody AnswerDTO answerDTO) throws ClubNotFoundException, QuestionNotFoundException {
        Answer savedAnswer = answerService.createAnswer(answerDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("Answer posted successfully", savedAnswer));
    }

    /**
     * Get Answers for a Question
     */
    @GetMapping("/{questionId}")
    public ResponseEntity<ApiResponseDTO<List<Answer>>> getAnswersByQuestion(@PathVariable Long questionId) {
        List<Answer> answers = answerService.getAnswersByQuestion(questionId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Answers fetched successfully", answers));
    }
}
