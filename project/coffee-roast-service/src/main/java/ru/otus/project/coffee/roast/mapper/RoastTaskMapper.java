package ru.otus.project.coffee.roast.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.project.coffee.roast.dto.RoastItemDto;
import ru.otus.project.coffee.roast.dto.rest.RoastTaskDto;
import ru.otus.project.coffee.roast.model.RoastItem;
import ru.otus.project.coffee.roast.model.RoastTask;

@SuppressWarnings("java:S6813")
@Mapper(componentModel = "spring", imports = RoastItemMapper.class)
public abstract class RoastTaskMapper {

    @Autowired
    private RoastItemMapper roastItemMapper;

    @Mapping(target = "id", source = "task.id")
    @Mapping(target = "roastStatus", source = "task.roastStatus")
    @Mapping(target = "roastItems", expression = "java(roastItemsToDtos(task.getRoastItems()))")
    public abstract RoastTaskDto roastTaskToDto(RoastTask task);

    public List<RoastItemDto> roastItemsToDtos(List<RoastItem> items) {
        return items.stream().map(roastItemMapper::roastItemToDto).toList();
    }
}
