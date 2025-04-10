package engine.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
public class CompletedQuizz  {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreatedDate
    LocalDateTime completedAt;

    @ManyToOne @JoinColumn(referencedColumnName = "email")
    MyUser user;

    @ManyToOne @JoinColumn(referencedColumnName = "id")
    Quiz quiz;

    public CompletedQuizz(MyUser user, Quiz quiz) {
        this.user = user;
        this.quiz = quiz;
        completedAt = LocalDateTime.now();
    }

    public CompletedQuizz() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime createdAt) {
        this.completedAt = createdAt;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}
