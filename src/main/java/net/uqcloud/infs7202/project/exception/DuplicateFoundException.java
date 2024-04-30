package net.uqcloud.infs7202.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class DuplicateFoundException extends ResponseStatusException {
    private static final HttpStatusCode STATUS_CODE = HttpStatus.BAD_REQUEST;

    public DuplicateFoundException(String reason) {
        super(STATUS_CODE, reason);
    }

    public DuplicateFoundException(String reason, Throwable cause) {
        super(STATUS_CODE, reason, cause);
    }
}
