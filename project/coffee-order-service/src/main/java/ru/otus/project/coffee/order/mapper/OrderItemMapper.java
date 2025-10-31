package ru.otus.project.coffee.order.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.otus.project.coffee.order.dto.kafka.RoastItemDto;
import ru.otus.project.coffee.order.dto.rest.OrderItemDto;
import ru.otus.project.coffee.order.model.OrderItem;

@Mapper(componentModel = "spring")
public abstract class OrderItemMapper {

    @Mapping(target = "coffee", source = "item.coffee.name")
    @Mapping(target = "roastDegree", source = "item.coffee.roastDegree.name")
    @Mapping(target = "packageSize", source = "item.packageSize.weight")
    @Mapping(target = "countOfPackages", source = "item.countOfPackages")
    public abstract OrderItemDto orderItemToOrderItemDto(OrderItem item);

    @Mapping(target = "coffee", source = "item.coffee.name")
    @Mapping(target = "roastDegree", source = "item.coffee.roastDegree.name")
    @Mapping(
            target = "weight",
            expression = "java(getWeightSum(item.getPackageSize().getWeight(), item.getCountOfPackages()))")
    public abstract RoastItemDto orderItemToRoastItemDto(OrderItem item);

    public int getWeightSum(int weight, int count) {
        return weight * count;
    }
}
