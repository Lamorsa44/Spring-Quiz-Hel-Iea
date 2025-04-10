package engine.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundModException extends RuntimeException {
    public NotFoundModException(String message) {
        super(message);
    }
}
