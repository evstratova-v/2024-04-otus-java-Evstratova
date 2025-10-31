package ru.otus.project.coffee.roast.service;

import java.util.List;
import ru.otus.project.coffee.roast.dto.kafka.RoastDto;
import ru.otus.project.coffee.roast.dto.rest.RoastTaskDto;
import ru.otus.project.coffee.roast.model.RoastStatus;

public interface RoastService {

    List<RoastTaskDto> findAll();

    void saveRoastTasks(List<RoastDto> roastDtos);

    RoastTaskDto changeStatus(long id, RoastStatus status);
}
