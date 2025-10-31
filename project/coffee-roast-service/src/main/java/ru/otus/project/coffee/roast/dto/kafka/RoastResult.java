package ru.otus.project.coffee.roast.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RoastResult {

    private long orderId;

    private long customerId;
}
