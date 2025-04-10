package engine.repository;

import engine.model.CompletedQuizz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompletedQuizzRepository extends JpaRepository<CompletedQuizz, Long> {
    void deleteByQuiz_Id(Long quizId);
    Page<CompletedQuizz> findAllByUser_EmailOrderByCompletedAtDesc(String userEmail, Pageable pageable);
}
