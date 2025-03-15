package ru.practicum.shareit.server.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.server.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
