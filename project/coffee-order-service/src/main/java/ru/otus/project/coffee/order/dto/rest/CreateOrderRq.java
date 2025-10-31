package ru.otus.project.coffee.order.dto.rest;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRq {

    private long addressId;

    private List<CreateOrderItemDto> orderItems;
}
