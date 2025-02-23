package org.studentclubmanagement.dtos;

public class AnswerDTO {
    private Long userId;
    private Long questionId;
    private Long clubId;
    private String answer;

    public AnswerDTO() {}

    public AnswerDTO(Long userId, Long questionId, Long clubId, String answer) {
        this.userId = userId;
        this.questionId = questionId;
        this.clubId = clubId;
        this.answer = answer;
    }

    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
