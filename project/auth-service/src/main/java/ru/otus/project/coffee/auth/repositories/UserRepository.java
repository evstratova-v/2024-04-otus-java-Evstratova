package ru.otus.project.coffee.auth.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.project.coffee.auth.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
