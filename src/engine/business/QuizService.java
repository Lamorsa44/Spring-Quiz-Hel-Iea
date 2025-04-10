package engine.business;

import engine.business.DTO.AnswerDTO;
import engine.business.DTO.CompletedQuizzDTO;
import engine.business.DTO.QuizWithoutAnswer;
import engine.business.exceptions.ForbiddenExceptionMod;
import engine.business.exceptions.InvalidFieldModException;
import engine.business.exceptions.NotFoundModException;
import engine.model.CompletedQuizz;
import engine.model.MyUser;
import engine.model.Quiz;
import engine.repository.CompletedQuizzRepository;
import engine.repository.QuizRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final CompletedQuizzRepository completedQuizzRepository;

    public QuizService(QuizRepository quizRepository, CompletedQuizzRepository completedQuizzRepository) {
        this.quizRepository = quizRepository;
        this.completedQuizzRepository = completedQuizzRepository;
    }

    public QuizWithoutAnswer saveQuiz(Quiz quiz) {
        if (quizRepository.existsByTitle(quiz.getTitle())) {
            if (quizRepository.findByTitle(quiz.getTitle()).stream().anyMatch(quiz::equals)) {
                throw new InvalidFieldModException("Quiz already exists");
            }
        }

        return new QuizWithoutAnswer(quizRepository.save(quiz));
    }

    public Page<QuizWithoutAnswer> getAllQuizzes(Pageable pageable) {
        return quizRepository.findAll(pageable).map(QuizWithoutAnswer::new);
    }

    public AnswerDTO checkAnswer(Collection<Integer> answer, long quizId, MyUser user) {
        Quiz quizz = quizRepository.findById(quizId).orElseThrow(
                () -> new NotFoundModException("No quizz with id found"));

        if (Set.of( quizz.getAnswer()).contains(answer)) {
            completedQuizzRepository.save(new CompletedQuizz(user, quizz));

            return new AnswerDTO(true, "Congratulations, you're right!");
        } else {
            return new AnswerDTO(false, "Wrong answer! Please, try again.");
        }
    }

    public QuizWithoutAnswer getQuizById(long id) {
        return new QuizWithoutAnswer(quizRepository.findById(id)
                .orElseThrow(() -> new NotFoundModException("No quizz found")));
    }

    @Transactional
    public void deleteWithId(long id, String user) {
        var quiz = quizRepository.findById(id).orElseThrow(
                () -> new NotFoundModException("No quizz found")
        );

        if (!quiz.getAuthor().equals(user)) {
            throw new ForbiddenExceptionMod("Not same author");
        }

        completedQuizzRepository.deleteByQuiz_Id(id);
        quizRepository.deleteById(id);
    }

    public Page<CompletedQuizzDTO> getCompletedQuizzes(String username, Pageable pageable) {
        return completedQuizzRepository.findAllByUser_EmailOrderByCompletedAtDesc(username, pageable)
                .map(CompletedQuizzDTO::new);
    }
}
