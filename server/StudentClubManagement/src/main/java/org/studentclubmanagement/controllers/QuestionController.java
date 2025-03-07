package org.studentclubmanagement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.studentclubmanagement.dtos.ApiResponseDTO;
import org.studentclubmanagement.dtos.QuestionRequestDTO;
import org.studentclubmanagement.dtos.QuestionResponseDTO;
import org.studentclubmanagement.exceptions.ClubNotFoundException;
import org.studentclubmanagement.exceptions.UndefinedUserClubException;
import org.studentclubmanagement.models.Question;
import org.studentclubmanagement.services.QuestionService;

import java.util.List;

@RestController
@RequestMapping("/questions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Question APIs", description = "APIs for managing questions in clubs, allowing users to post and retrieve questions.")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * Creates a new question within a club.
     *
     * @param questionRequestDTO The question details including club ID, title, and content.
     * @return A response entity containing a success message and the created question.
     * @throws ClubNotFoundException If the specified club does not exist.
     * @throws UndefinedUserClubException If the user is not a member of the club.
     */
    @PostMapping
    @Operation(
        summary = "Create a Question",
        description = "Allows users to post a question within a specific club."
    )
    public ResponseEntity<ApiResponseDTO> createQuestion(@RequestBody QuestionRequestDTO questionRequestDTO)
            throws ClubNotFoundException, UndefinedUserClubException {
        Question question = questionService.createQuestion(questionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("Question created successfully", question));
    }

    /**
     * Retrieves all questions posted in a specific club.
     *
     * @param clubId The unique ID of the club whose questions need to be fetched.
     * @return A response entity containing a list of questions for the specified club.
     */
    @GetMapping("/{clubId}")
    @Operation(
        summary = "Get Questions by Club ID",
        description = "Fetches all questions associated with a specific club."
    )
    public ResponseEntity<ApiResponseDTO<List<QuestionResponseDTO>>> getQuestionsByClub(
        @Parameter(description = "The unique ID of the club whose questions need to be retrieved", required = true, example = "1")
        @PathVariable int clubId
    ) {
        List<QuestionResponseDTO> questions = questionService.getQuestionsByClub(clubId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Questions fetched successfully", questions));
    }
}
