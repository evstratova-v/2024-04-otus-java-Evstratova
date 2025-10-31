package ru.otus.project.coffee.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.project.coffee.order.exception.CustomerNotFoundException;
import ru.otus.project.coffee.order.repository.CustomerRepository;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public long findCustomerIdByLogin(String login) {
        return customerRepository
                .findByLogin(login)
                .orElseThrow(() -> new CustomerNotFoundException(login))
                .getId();
    }
}
