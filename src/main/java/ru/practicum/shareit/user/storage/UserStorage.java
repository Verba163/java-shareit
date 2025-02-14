package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

interface UserStorage {

    User addUser(User user);

    Collection<User> getAllUsers();

    void deleteUser(long id);

    User updateUser(User user);

    User getUserById(long id);
}
