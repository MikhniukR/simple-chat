package ru.mikhniuk.clients;

import ru.mikhniuk.models.User;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class UserClientInMemory implements UserClient {
    private List<User> users;

    public UserClientInMemory() {
        this.users = new LinkedList<>();
    }

    @Override
    public List<User> getAllUsers() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public User createUser(String nick) throws IllegalArgumentException {
        if(users.stream().anyMatch(
                user -> user.getNick().equals(nick))) {
            throw new IllegalArgumentException();
        }

        User user = new User(nick);
        users.add(user);

        return new User(user);
    }

    @Override
    public boolean deleteUser(String nick) throws NoSuchElementException {
        if(users.stream().noneMatch(
                user -> user.getNick().equals(nick))) {
            throw new NoSuchElementException();
        }
        return users.removeIf(
                user -> user.getNick().equals(nick)
        );
    }
}
