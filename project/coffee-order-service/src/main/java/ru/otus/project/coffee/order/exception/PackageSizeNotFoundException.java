package ru.otus.project.coffee.order.exception;

public class PackageSizeNotFoundException extends RuntimeException {

    public PackageSizeNotFoundException(int id) {
        super("Package size with id %s not found".formatted(id));
    }
}
