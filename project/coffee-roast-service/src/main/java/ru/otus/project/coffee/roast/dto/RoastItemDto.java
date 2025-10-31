package ru.otus.project.coffee.roast.dto;

import lombok.Data;

@Data
public class RoastItemDto {

    private long id;

    private String coffee;

    private String roastDegree;

    private int weight;
}
