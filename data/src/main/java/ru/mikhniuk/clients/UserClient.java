package ru.mikhniuk.clients;

import ru.mikhniuk.models.User;

import java.util.List;
import java.util.NoSuchElementException;

public interface UserClient {
    List<User> getAllUsers();
    User createUser(String nick) throws IllegalArgumentException;
    boolean deleteUser(String nick) throws NoSuchElementException;
}
