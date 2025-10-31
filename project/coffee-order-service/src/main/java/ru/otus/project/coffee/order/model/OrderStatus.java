package ru.otus.project.coffee.order.model;

import lombok.Getter;

@Getter
public enum OrderStatus {
    NEW,
    SENT_FOR_ROAST,
    READY
}
