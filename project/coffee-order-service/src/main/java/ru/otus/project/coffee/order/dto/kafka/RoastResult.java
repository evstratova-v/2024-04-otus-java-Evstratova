package ru.otus.project.coffee.order.dto.kafka;

import lombok.Data;

@Data
public class RoastResult {

    private long orderId;

    private long customerId;
}
