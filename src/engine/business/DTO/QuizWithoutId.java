package engine.business.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record QuizWithoutId(@NotBlank(message = "Title shouldn't be empty") String title
        , @NotBlank(message = "Text shouldn't be empty") String text
        , @NotNull @Size(min = 2, message = "Minimum 2 options") List<String> options, List<Integer> answer) {
}
