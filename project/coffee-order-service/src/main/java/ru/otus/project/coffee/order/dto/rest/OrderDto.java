package ru.otus.project.coffee.order.dto.rest;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.project.coffee.order.model.OrderStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto {

    private long orderId;

    private OrderStatus orderStatus;

    private AddressDto address;

    private List<OrderItemDto> orderItems;
}
