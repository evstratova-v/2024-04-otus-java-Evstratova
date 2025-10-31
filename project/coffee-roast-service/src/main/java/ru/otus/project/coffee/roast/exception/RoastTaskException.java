package ru.otus.project.coffee.roast.exception;

public class RoastTaskException extends RuntimeException {
    public RoastTaskException(long id) {
        super("RoastTask with id %s not found".formatted(id));
    }
}
