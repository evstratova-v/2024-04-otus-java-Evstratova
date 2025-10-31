package ru.otus.project.coffee.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.project.coffee.order.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {}
