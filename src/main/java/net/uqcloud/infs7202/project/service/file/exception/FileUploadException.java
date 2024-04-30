package net.uqcloud.infs7202.project.service.file.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FileUploadException extends ResponseStatusException {
    public FileUploadException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file");
    }
    public FileUploadException(Throwable e) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file", e);
    }
}
