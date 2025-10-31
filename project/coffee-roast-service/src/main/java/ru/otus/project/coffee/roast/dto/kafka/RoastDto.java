package ru.otus.project.coffee.roast.dto.kafka;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.project.coffee.roast.dto.RoastItemDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoastDto {

    private long orderId;

    private long customerId;

    private List<RoastItemDto> roastItems;
}
