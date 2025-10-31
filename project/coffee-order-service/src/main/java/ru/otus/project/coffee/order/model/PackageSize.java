package ru.otus.project.coffee.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "package_size")
public class PackageSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int weight;
}
