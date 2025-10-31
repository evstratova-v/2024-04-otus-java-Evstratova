package ru.otus.project.coffee.order.exception;

public class AddressNotFoundException extends RuntimeException {

    public AddressNotFoundException(long id) {
        super("Address with id %s not found".formatted(id));
    }
}
