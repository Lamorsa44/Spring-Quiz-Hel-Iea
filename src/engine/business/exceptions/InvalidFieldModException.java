package engine.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidFieldModException extends RuntimeException {
    public InvalidFieldModException(String message) {
        super(message);
    }
}
