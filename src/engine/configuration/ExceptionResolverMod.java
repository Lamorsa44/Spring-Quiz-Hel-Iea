package engine.configuration;

import engine.business.exceptions.ForbiddenExceptionMod;
import engine.business.exceptions.InvalidFieldModException;
import engine.business.exceptions.NotFoundModException;
import engine.business.exceptions.UnauthorizedExceptionMod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class ExceptionResolverMod extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
            , HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = Arrays.stream(ex.getDetailMessageArguments())
                .reduce("", (ac, s) -> ac.toString().isBlank() ? s : ac.toString() + s);

        return ResponseEntity
                .status(status)
                .body(errors);
    }

    @ExceptionHandler(InvalidFieldModException.class)
    public ResponseEntity<Object> handleInvalidFieldModException(InvalidFieldModException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedExceptionMod.class)
    public ResponseEntity<Object> handleUnauthorizedExceptionMod(UnauthorizedExceptionMod ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundModException.class)
    public ResponseEntity<Object> handleNotFoundModException(NotFoundModException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenExceptionMod.class)
    public ResponseEntity<Object> handleForbiddenExceptionMod(ForbiddenExceptionMod ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }
}
