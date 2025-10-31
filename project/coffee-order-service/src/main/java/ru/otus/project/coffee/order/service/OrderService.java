package ru.otus.project.coffee.order.service;

import java.util.List;
import ru.otus.project.coffee.order.dto.kafka.RoastDto;
import ru.otus.project.coffee.order.dto.kafka.RoastResult;
import ru.otus.project.coffee.order.dto.rest.CreateOrderRq;
import ru.otus.project.coffee.order.dto.rest.CreateOrderRs;
import ru.otus.project.coffee.order.dto.rest.OrderDto;
import ru.otus.project.coffee.order.model.OrderStatus;

public interface OrderService {

    CreateOrderRs createOrder(CreateOrderRq createOrderRq, String login);

    List<OrderDto> findOrderByLogin(String login);

    List<RoastDto> getRoastRequestForTop10NewOrders();

    void changeOrderStatus(long orderId, OrderStatus newStatus);

    void changeOrdersStatus(List<RoastResult> roastResult, OrderStatus newStatus);

    long countNewOrders();
}
