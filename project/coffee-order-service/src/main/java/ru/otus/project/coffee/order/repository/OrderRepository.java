package ru.otus.project.coffee.order.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.project.coffee.order.model.CoffeeOrder;
import ru.otus.project.coffee.order.model.Customer;
import ru.otus.project.coffee.order.model.OrderStatus;

public interface OrderRepository extends JpaRepository<CoffeeOrder, Long> {

    List<CoffeeOrder> findByCustomerOrderByIdDesc(Customer customer);

    long countByOrderStatus(OrderStatus orderStatus);

    List<CoffeeOrder> findTop10ByOrderStatusOrderById(OrderStatus orderStatus);
}
