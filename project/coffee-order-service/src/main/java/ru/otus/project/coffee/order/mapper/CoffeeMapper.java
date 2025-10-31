package ru.otus.project.coffee.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.project.coffee.order.dto.rest.CoffeeDto;
import ru.otus.project.coffee.order.model.Coffee;

@Mapper(componentModel = "spring")
public interface CoffeeMapper {

    @Mapping(target = "id", source = "coffee.id")
    @Mapping(target = "name", source = "coffee.name")
    @Mapping(target = "roastDegree.id", source = "coffee.roastDegree.id")
    @Mapping(target = "roastDegree.name", source = "coffee.roastDegree.name")
    CoffeeDto coffeeToCoffeeDto(Coffee coffee);
}
