package engine.business.DTO;

import engine.model.CompletedQuizz;

import java.time.format.DateTimeFormatter;

public record CompletedQuizzDTO(long id, String completedAt) {
    public CompletedQuizzDTO(CompletedQuizz completedQuizz) {
        this(
                completedQuizz.getQuiz().getId(),
                completedQuizz.getCompletedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
