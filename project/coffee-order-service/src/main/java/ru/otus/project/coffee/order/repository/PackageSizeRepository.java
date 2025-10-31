package ru.otus.project.coffee.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.project.coffee.order.model.PackageSize;

public interface PackageSizeRepository extends JpaRepository<PackageSize, Integer> {}
