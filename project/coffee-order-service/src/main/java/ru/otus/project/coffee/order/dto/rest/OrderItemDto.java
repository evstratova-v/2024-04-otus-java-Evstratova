package ru.otus.project.coffee.order.dto.rest;

import lombok.Data;

@Data
public class OrderItemDto {

    private String coffee;

    private String roastDegree;

    private int packageSize;

    private int countOfPackages;
}
