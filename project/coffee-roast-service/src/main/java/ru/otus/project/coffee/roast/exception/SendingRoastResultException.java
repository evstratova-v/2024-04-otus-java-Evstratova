package ru.otus.project.coffee.roast.exception;

public class SendingRoastResultException extends RuntimeException {

    public SendingRoastResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendingRoastResultException(Throwable cause) {
        super(cause);
    }
}
