package ru.simple.chat.clients;

import ru.simple.chat.models.User;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The type User client in memory.
 */
public class UserClientInMemory implements UserClient {
    private List<User> users;

    /**
     * Instantiates a new User client in memory.
     */
    public UserClientInMemory() {
        this.users = new LinkedList<>();
    }

    @Override
    public List<User> getAllUsers() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public User createUser(String nick) throws IllegalArgumentException {
        if (contains(nick)) {
            throw new IllegalArgumentException("User with nick " + nick + " already exist, nick should be uniq.");
        }

        User user = new User(nick);
        users.add(user);

        return new User(user);
    }

    @Override
    public boolean deleteUser(String nick) throws NoSuchElementException {
        if (!contains(nick)) {
            throw new NoSuchElementException("No user with nick " + nick);
        }

        return users.removeIf(
                user -> user.getNick().equals(nick)
        );
    }

    @Override
    public User getByNick(String nick) throws NoSuchElementException {
        if (!contains(nick)) {
            throw new NoSuchElementException("No user with nick " + nick);
        }

        return new User(users.stream().filter(
                user -> user.getNick().equals(nick))
                .findFirst().get());
    }

    @Override
    public boolean contains(String nick) {
        return users.stream().anyMatch(
                user -> user.getNick().equals(nick));
    }
}
