package engine.repository;

import engine.model.Quiz;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    boolean existsByTitle(@NotBlank(message = "Title can't be black") String title);
    List<Quiz> findByTitle(@NotBlank(message = "Title can't be blank") String title);
}
