package io.monitoring.model.payload;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
public class ExceptionResponse {
    private final Date timestamp;
    private final int code;
    private final String status;
    private final String message;

    public ExceptionResponse(Throwable e, HttpStatus status) {
        this.timestamp = new Date();
        this.code = status.value();
        this.status = status.name();
        this.message = e.getMessage();
    }
}
