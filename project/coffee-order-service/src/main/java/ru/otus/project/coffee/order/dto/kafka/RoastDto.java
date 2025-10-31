package ru.otus.project.coffee.order.dto.kafka;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoastDto {

    private long orderId;

    private long customerId;

    private List<RoastItemDto> roastItems;
}
