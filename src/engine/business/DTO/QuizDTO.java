package engine.business.DTO;

import java.util.Collection;

public record QuizDTO(String title, String text, Collection<String> options) {}
