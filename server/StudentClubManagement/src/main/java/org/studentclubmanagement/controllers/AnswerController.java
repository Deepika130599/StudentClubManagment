package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/user/answers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Answer APIs", description = "APIs for managing answers to questions in a club, allowing users to post and retrieve answers.")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    /**
     * Creates a new answer for a question.
     *
     * @param answerDTO The answer details including the question ID, content, and author.
     * @return A response entity containing a success message and the created answer.
     * @throws ClubNotFoundException If the specified club does not exist.
     * @throws QuestionNotFoundException If the specified question does not exist.
     */
    @PostMapping
    @Operation(
        summary = "Create an Answer",
        description = "Allows users to post an answer to a specific question in a club."
    )
    public ResponseEntity<ApiResponseDTO> createAnswer(@RequestBody AnswerDTO answerDTO)
            throws ClubNotFoundException, QuestionNotFoundException {
        Answer savedAnswer = answerService.createAnswer(answerDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("Answer posted successfully", savedAnswer));
    }

    /**
     * Retrieves all answers for a specific question.
     *
     * @param questionId The unique ID of the question of whose answers need to be fetched.
     * @return A response entity containing a list of answers for the specified question.
     */
    @GetMapping("/{questionId}")
    @Operation(
        summary = "Get Answers for a Question",
        description = "Fetches all answers associated with a specific question ID."
    )
    public ResponseEntity<ApiResponseDTO<List<Answer>>> getAnswersByQuestion(
        @Parameter(
            description = "The unique ID of the question whose answers need to be retrieved",
            required = true,
            example = "1"
        )
        @PathVariable Long questionId
    ) {
        List<Answer> answers = answerService.getAnswersByQuestion(questionId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Answers fetched successfully", answers));
    }
}
