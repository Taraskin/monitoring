package io.monitoring.model.payload;

import lombok.Getter;

@Getter
public class MessageResponse {
    private final String message;

    public MessageResponse(String message) {
        this.message = message;
    }
}
