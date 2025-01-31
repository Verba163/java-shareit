package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    long currentId = 1;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        checkEmailUnique(user.getEmail());
        user.setId(currentId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        log.debug("Got all users. Total: {}", users.size());
        return users.values();
    }

    @Override
    public void deleteUser(long id) {
        User user = getUserById(id);
        if (user == null) {
            throw new NotFoundException(String.format("User with id %d not found", id));
        }
        log.debug("User with Id {} deleted", id);
        users.remove(id);
    }

    @Override
    public User updateUser(User user) {

        if (!users.containsKey(user.getId())) {
            throw new NotFoundException(String.format("User with id %d not found", user.getId()));
        }

        checkEmailUnique(user.getEmail());
        log.debug("User with Id {} updated", user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException(String.format("User with id %d not found", id));
        }
        log.debug("Got User with Id {}", id);
        return user;
    }

    private void checkEmailUnique(String email) {
        for (User existingUser : users.values()) {
            String existingEmail = existingUser.getEmail();
            if (existingEmail != null && existingEmail.equals(email)) {
                throw new IllegalArgumentException(String.format("User with the same email is already registered: %s", email));
            }
        }
    }

    public boolean userExists(long id) {
        return users.containsKey(id);
    }
}
