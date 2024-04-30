package net.uqcloud.infs7202.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class DataNotFoundException extends ResponseStatusException {
    private static final HttpStatusCode STATUS_CODE = HttpStatus.NOT_FOUND;

    public DataNotFoundException(String reason) {
        super(STATUS_CODE, reason);
    }

    public DataNotFoundException(String reason, Throwable cause) {
        super(STATUS_CODE, reason, cause);
    }
}
