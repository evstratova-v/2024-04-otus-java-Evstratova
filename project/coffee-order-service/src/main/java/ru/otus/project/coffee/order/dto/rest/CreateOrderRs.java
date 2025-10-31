package ru.otus.project.coffee.order.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.project.coffee.order.model.OrderStatus;

@AllArgsConstructor
@Data
public class CreateOrderRs {

    private long orderId;

    private OrderStatus orderStatus;
}
