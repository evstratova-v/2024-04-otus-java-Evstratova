package ru.otus.project.coffee.order.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.project.coffee.order.dto.rest.CoffeeDto;
import ru.otus.project.coffee.order.service.CoffeeService;

@RequiredArgsConstructor
@RestController
public class CoffeeController {

    private final CoffeeService coffeeService;

    @GetMapping("/api/v1/coffee")
    public List<CoffeeDto> getAllCoffee(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "roastDegreeIds", required = false) List<Long> roastDegreeIds) {
        return coffeeService.findAllBySpecification(name, roastDegreeIds);
    }

    @GetMapping("/api/v1/coffee/{id}")
    public CoffeeDto getCoffee(@PathVariable("id") long id) {
        return coffeeService.findById(id);
    }
}
