package io.monitoring.exception;

public class ItemNotFoundException extends Throwable {
    private static final String MSG_TEMPLATE = "Item with ID = %d not found!";

    public ItemNotFoundException(Long id) {
        super(String.format(MSG_TEMPLATE, id));
    }
}
