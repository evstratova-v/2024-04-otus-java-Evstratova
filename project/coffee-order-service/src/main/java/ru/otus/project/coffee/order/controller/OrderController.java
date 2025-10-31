package ru.otus.project.coffee.order.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.coffee.order.dto.rest.CreateOrderRq;
import ru.otus.project.coffee.order.dto.rest.CreateOrderRs;
import ru.otus.project.coffee.order.dto.rest.OrderDto;
import ru.otus.project.coffee.order.service.OrderService;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/api/v1/order")
    public List<OrderDto> getOrder(@AuthenticationPrincipal Jwt principal) {
        return orderService.findOrderByLogin(principal.getClaim("sub").toString());
    }

    @PostMapping("/api/v1/order")
    public CreateOrderRs createOrder(@RequestBody CreateOrderRq createOrderRq, @AuthenticationPrincipal Jwt principal) {
        var login = principal.getClaim("sub").toString();
        return orderService.createOrder(createOrderRq, login);
    }
}
