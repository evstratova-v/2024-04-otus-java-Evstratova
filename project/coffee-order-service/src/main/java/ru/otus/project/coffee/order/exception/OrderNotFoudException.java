package ru.otus.project.coffee.order.exception;

public class OrderNotFoudException extends RuntimeException {
    public OrderNotFoudException(long id) {
        super("Order with id %s not foud".formatted(id));
    }
}
