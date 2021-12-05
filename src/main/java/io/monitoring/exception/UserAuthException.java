package io.monitoring.exception;

public class UserAuthException extends Throwable {
    public UserAuthException(String message) {
        super(message);
    }

    public UserAuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
