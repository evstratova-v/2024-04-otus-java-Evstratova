package ru.otus.project.coffee.roast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.project.coffee.roast.model.RoastTask;

public interface RoastTaskRepository extends JpaRepository<RoastTask, Long> {}
