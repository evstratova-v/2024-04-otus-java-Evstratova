package ru.otus.project.coffee.roast.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.project.coffee.roast.dto.RoastItemDto;
import ru.otus.project.coffee.roast.model.RoastItem;

@Mapper(componentModel = "spring")
public interface RoastItemMapper {

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "coffee", source = "item.coffee")
    @Mapping(target = "roastDegree", source = "item.roastDegree")
    @Mapping(target = "weight", source = "item.weight")
    RoastItemDto roastItemToDto(RoastItem item);
}
