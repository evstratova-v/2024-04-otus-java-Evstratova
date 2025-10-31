package ru.otus.project.coffee.order.service;

import static ru.otus.project.coffee.order.repository.spec.CoffeeSpecification.hasNameLike;
import static ru.otus.project.coffee.order.repository.spec.CoffeeSpecification.hasRoastDegreeIn;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.coffee.order.dto.rest.CoffeeDto;
import ru.otus.project.coffee.order.exception.CoffeeNotFoundException;
import ru.otus.project.coffee.order.mapper.CoffeeMapper;
import ru.otus.project.coffee.order.model.Coffee;
import ru.otus.project.coffee.order.repository.CoffeeRepository;

@RequiredArgsConstructor
@Service
public class CoffeeServiceImpl implements CoffeeService {

    private final CoffeeRepository coffeeRepository;

    private final CoffeeMapper coffeeMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CoffeeDto> findAllBySpecification(String name, List<Long> roastDegreeIds) {
        boolean isNameSpec = name != null && !name.isBlank();
        boolean isRoastDegreeSpec = roastDegreeIds != null && !roastDegreeIds.isEmpty();
        List<Specification<Coffee>> specifications = new ArrayList<>();
        List<Coffee> coffee;

        if (isNameSpec) {
            specifications.add(hasNameLike(name));
        }
        if (isRoastDegreeSpec) {
            specifications.add(hasRoastDegreeIn(roastDegreeIds));
        }
        var specification = specifications.stream().reduce(Specification::and);
        coffee = specification.map(coffeeRepository::findAll).orElseGet(coffeeRepository::findAll);

        return coffee.stream().map(coffeeMapper::coffeeToCoffeeDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public CoffeeDto findById(long id) {
        return coffeeRepository
                .findById(id)
                .map(coffeeMapper::coffeeToCoffeeDto)
                .orElseThrow(() -> new CoffeeNotFoundException(id));
    }
}
