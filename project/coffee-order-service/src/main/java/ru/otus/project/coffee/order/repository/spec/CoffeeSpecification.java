package ru.otus.project.coffee.order.repository.spec;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.project.coffee.order.model.Coffee;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CoffeeSpecification {

    public static Specification<Coffee> hasNameLike(String name) {
        return (root, query, cb) -> cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Coffee> hasRoastDegreeIn(List<Long> roastDegreeIds) {
        return (root, query, cb) -> root.get("roastDegree").get("id").in(roastDegreeIds);
    }
}
