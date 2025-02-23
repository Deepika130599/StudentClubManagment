package org.studentclubmanagement.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuestionRequestDTO {

    @NotNull
    private Long userId;
    @NotNull
    private Long clubId;
    @NotBlank(message = "Title cannot be blank")
    private String title;
    @NotBlank(message = "Question content cannot be blank")
    private String question;

    public QuestionRequestDTO() {}

    public QuestionRequestDTO(Long userId, Long clubId, String title, String question) {
        this.userId = userId;
        this.clubId = clubId;
        this.title = title;
        this.question = question;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
