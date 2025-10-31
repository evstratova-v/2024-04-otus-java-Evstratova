package ru.otus.project.coffee.order.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String login) {
        super("Customer with login %s not found".formatted(login));
    }
}
