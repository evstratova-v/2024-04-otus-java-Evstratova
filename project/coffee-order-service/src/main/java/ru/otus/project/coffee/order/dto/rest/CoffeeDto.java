package ru.otus.project.coffee.order.dto.rest;

import lombok.Data;

@Data
public class CoffeeDto {

    private long id;

    private String name;

    private RoastDegreeDto roastDegree;
}
