package ru.otus.project.coffee.order.dto.kafka;

import lombok.Data;

@Data
public class RoastItemDto {

    private String coffee;

    private String roastDegree;

    private int weight;
}
