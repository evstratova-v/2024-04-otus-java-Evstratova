package ru.otus.project.coffee.roast.dto.rest;

import java.util.List;
import lombok.Data;
import ru.otus.project.coffee.roast.dto.RoastItemDto;
import ru.otus.project.coffee.roast.model.RoastStatus;

@Data
public class RoastTaskDto {

    private long id;

    private RoastStatus roastStatus;

    private List<RoastItemDto> roastItems;
}
