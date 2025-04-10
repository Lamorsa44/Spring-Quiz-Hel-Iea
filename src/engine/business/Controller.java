package engine.business;

import engine.business.DTO.*;
import engine.model.MyUser;
import engine.model.Quiz;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    private final QuizService quizService;
    private final MyUserService myUserService;

    public Controller(QuizService quizService, MyUserService myUserService) {
        this.quizService = quizService;
        this.myUserService = myUserService;
    }

    @GetMapping("api/quizzes")
    public Page<QuizWithoutAnswer> AllQuizzes(@PageableDefault Pageable pageable) {
        return quizService.getAllQuizzes(pageable);
    }

    @GetMapping("api/quizzes/completed")
    public Page<CompletedQuizzDTO> getCompletedQuizzes(
            @PageableDefault Pageable pageable,
            @AuthenticationPrincipal MyUserAdapter user) {
        return quizService.getCompletedQuizzes(user.getUsername(), pageable);
    }

    @GetMapping("api/quizzes/{id}")
    public QuizWithoutAnswer GetQuiz(@PathVariable(name = "id") long id) {
        return quizService.getQuizById(id);
    }

    @PostMapping("api/quizzes")
    public QuizWithoutAnswer CreateQuiz(@Valid @RequestBody QuizWithoutId quiz) {
        return quizService.saveQuiz(new Quiz(quiz));
    }

    @PostMapping("api/quizzes/{id}/solve")
    public AnswerDTO solveQuiz(
            @RequestBody JustAnswer answer,
            @PathVariable(name = "id") long id,
            @AuthenticationPrincipal MyUserAdapter adapter) {
        return quizService.checkAnswer(answer.answer(), id, adapter.getMyUser());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("api/quizzes/{id}")
    public void DeleteQuiz(
            @PathVariable(name = "id") long id,
            @AuthenticationPrincipal MyUserAdapter user) {
        quizService.deleteWithId(id, user.getUsername());
    }

    // Auth

    @GetMapping("test/users")
    public List<MyUser> getAllUsers() {
        return myUserService.getAllUsersYia();
    }

    @PostMapping("api/register")
    public void registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        myUserService.saveUser(registerRequest);
    }
}
