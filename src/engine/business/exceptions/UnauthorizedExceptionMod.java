package engine.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedExceptionMod extends RuntimeException {
    public UnauthorizedExceptionMod(String message) {
        super(message);
    }
}
