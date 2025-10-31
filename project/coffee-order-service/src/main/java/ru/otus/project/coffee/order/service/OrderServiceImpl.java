package ru.otus.project.coffee.order.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.project.coffee.order.dto.kafka.RoastDto;
import ru.otus.project.coffee.order.dto.kafka.RoastItemDto;
import ru.otus.project.coffee.order.dto.kafka.RoastResult;
import ru.otus.project.coffee.order.dto.rest.AddressDto;
import ru.otus.project.coffee.order.dto.rest.CreateOrderRq;
import ru.otus.project.coffee.order.dto.rest.CreateOrderRs;
import ru.otus.project.coffee.order.dto.rest.OrderDto;
import ru.otus.project.coffee.order.dto.rest.OrderItemDto;
import ru.otus.project.coffee.order.exception.AddressNotFoundException;
import ru.otus.project.coffee.order.exception.CoffeeNotFoundException;
import ru.otus.project.coffee.order.exception.CustomerNotFoundException;
import ru.otus.project.coffee.order.exception.OrderNotFoudException;
import ru.otus.project.coffee.order.exception.PackageSizeNotFoundException;
import ru.otus.project.coffee.order.mapper.OrderItemMapper;
import ru.otus.project.coffee.order.model.Address;
import ru.otus.project.coffee.order.model.Coffee;
import ru.otus.project.coffee.order.model.CoffeeOrder;
import ru.otus.project.coffee.order.model.Customer;
import ru.otus.project.coffee.order.model.OrderItem;
import ru.otus.project.coffee.order.model.OrderStatus;
import ru.otus.project.coffee.order.model.PackageSize;
import ru.otus.project.coffee.order.repository.AddressRepository;
import ru.otus.project.coffee.order.repository.CoffeeRepository;
import ru.otus.project.coffee.order.repository.CustomerRepository;
import ru.otus.project.coffee.order.repository.OrderRepository;
import ru.otus.project.coffee.order.repository.PackageSizeRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final CoffeeRepository coffeeRepository;

    private final CustomerRepository customerRepository;

    private final AddressRepository addressRepository;

    private final PackageSizeRepository packageSizeRepository;

    private final OrderRepository orderRepository;

    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public CreateOrderRs createOrder(CreateOrderRq createOrderRq, String login) {
        Customer customer =
                customerRepository.findByLogin(login).orElseThrow(() -> new CustomerNotFoundException(login));
        long addressId = createOrderRq.getAddressId();
        Address address =
                addressRepository.findById(addressId).orElseThrow(() -> new AddressNotFoundException(addressId));

        List<OrderItem> orderItems = createOrderRq.getOrderItems().stream()
                .map(orderItemDto -> {
                    long coffeeId = orderItemDto.getCoffeeId();
                    int packageSizeId = orderItemDto.getPackageSizeId();
                    Coffee coffee = coffeeRepository
                            .findById(coffeeId)
                            .orElseThrow(() -> new CoffeeNotFoundException(coffeeId));
                    PackageSize packageSize = packageSizeRepository
                            .findById(packageSizeId)
                            .orElseThrow(() -> new PackageSizeNotFoundException(packageSizeId));

                    return new OrderItem(coffee, packageSize, orderItemDto.getCountOfPackages());
                })
                .toList();

        CoffeeOrder coffeeOrder = new CoffeeOrder(0, customer, address, orderItems, OrderStatus.NEW);
        long orderId = orderRepository.save(coffeeOrder).getId();

        return new CreateOrderRs(orderId, coffeeOrder.getOrderStatus());
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderDto> findOrderByLogin(String login) {
        Customer customer =
                customerRepository.findByLogin(login).orElseThrow(() -> new CustomerNotFoundException(login));
        return orderRepository.findByCustomerOrderByIdDesc(customer).stream()
                .map(order -> {
                    Address address = order.getAddress();
                    AddressDto addressDto = new AddressDto(
                            address.getCity(),
                            address.getStreet(),
                            address.getHouseNumber(),
                            address.getApartmentNumber());
                    List<OrderItem> orderItems = order.getOrderItems();
                    List<OrderItemDto> orderItemDtos = orderItems.stream()
                            .map(orderItemMapper::orderItemToOrderItemDto)
                            .toList();
                    return new OrderDto(order.getId(), order.getOrderStatus(), addressDto, orderItemDtos);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoastDto> getRoastRequestForTop10NewOrders() {
        List<RoastDto> roastDtos = new ArrayList<>();
        List<CoffeeOrder> coffeeOrders = orderRepository.findTop10ByOrderStatusOrderById(OrderStatus.NEW);
        for (CoffeeOrder coffeeOrder : coffeeOrders) {
            long orderId = coffeeOrder.getId();
            List<OrderItem> orderItems = coffeeOrder.getOrderItems();
            List<RoastItemDto> roastItemDto = orderItems.stream()
                    .map(orderItemMapper::orderItemToRoastItemDto)
                    .toList();
            RoastDto roastDto = new RoastDto(orderId, coffeeOrder.getCustomer().getId(), roastItemDto);
            roastDtos.add(roastDto);
        }
        return roastDtos;
    }

    @Transactional
    @Override
    public void changeOrderStatus(long orderId, OrderStatus newStatus) {
        logChangeStatus(orderId, newStatus);
        CoffeeOrder coffeeOrder =
                orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoudException(orderId));
        coffeeOrder.setOrderStatus(newStatus);
        orderRepository.save(coffeeOrder);
    }

    @Transactional
    @Override
    public void changeOrdersStatus(List<RoastResult> roastResults, OrderStatus newStatus) {
        for (var roastResult : roastResults) {
            var orderId = roastResult.getOrderId();
            logChangeStatus(orderId, newStatus);
            CoffeeOrder coffeeOrder =
                    orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoudException(orderId));
            coffeeOrder.setOrderStatus(newStatus);
            orderRepository.save(coffeeOrder);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public long countNewOrders() {
        return orderRepository.countByOrderStatus(OrderStatus.NEW);
    }

    private void logChangeStatus(long orderId, OrderStatus newStatus) {
        log.info("change status, order id {}, new status {}", orderId, newStatus);
    }
}
