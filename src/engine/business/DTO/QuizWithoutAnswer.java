package engine.business.DTO;

import engine.model.Quiz;

import java.util.List;

public record QuizWithoutAnswer(long id, String title, String text, List<String> options) {
    public QuizWithoutAnswer(Quiz quiz) {
        this(quiz.getId(), quiz.getTitle(), quiz.getText(), quiz.getOptions());
    }
}
