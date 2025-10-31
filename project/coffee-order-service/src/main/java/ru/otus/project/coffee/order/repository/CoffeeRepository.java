package ru.otus.project.coffee.order.repository;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.project.coffee.order.model.Coffee;

public interface CoffeeRepository extends JpaRepository<Coffee, Long>, JpaSpecificationExecutor<Coffee> {

    @Override
    @EntityGraph(value = "coffee-roast-entity-graph")
    List<Coffee> findAll();

    @Override
    @EntityGraph(value = "coffee-roast-entity-graph")
    List<Coffee> findAll(Specification<Coffee> specification);
}
