package org.studentclubmanagement.controllers;

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
@RequestMapping("api/questions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Question APIs", description = "Operations related to questions.")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createQuestion(@RequestBody QuestionRequestDTO questionRequestDTO) throws ClubNotFoundException, UndefinedUserClubException {
        Question question = questionService.createQuestion(questionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>("Question created successfully", question));
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<ApiResponseDTO<List<QuestionResponseDTO>>> getQuestionsByClub(@PathVariable int clubId) {
        List<QuestionResponseDTO> questions = questionService.getQuestionsByClub(clubId);
        return ResponseEntity.ok(new ApiResponseDTO<>("Questions fetched successfully", questions));
    }


}
