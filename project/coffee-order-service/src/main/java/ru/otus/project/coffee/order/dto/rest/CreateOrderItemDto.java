package ru.otus.project.coffee.order.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateOrderItemDto {

    private long coffeeId;

    private int packageSizeId;

    private int countOfPackages;
}
