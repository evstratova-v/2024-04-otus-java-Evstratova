package ru.otus.project.coffee.order.service;

import java.util.List;
import ru.otus.project.coffee.order.dto.rest.CoffeeDto;

public interface CoffeeService {

    List<CoffeeDto> findAllBySpecification(String name, List<Long> roastDegreeIds);

    CoffeeDto findById(long id);
}
