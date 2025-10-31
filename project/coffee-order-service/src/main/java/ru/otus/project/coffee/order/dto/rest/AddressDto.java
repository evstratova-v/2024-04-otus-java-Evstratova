package ru.otus.project.coffee.order.dto.rest;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AddressDto {

    private String city;

    private String street;

    private String houseNumber;

    private String apartmentNumber;
}
