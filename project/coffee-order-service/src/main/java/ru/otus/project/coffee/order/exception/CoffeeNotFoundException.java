package ru.otus.project.coffee.order.exception;

public class CoffeeNotFoundException extends RuntimeException {

    public CoffeeNotFoundException(long id) {
        super("Coffee with id %s not found".formatted(id));
    }
}
