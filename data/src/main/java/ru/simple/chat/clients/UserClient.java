package ru.simple.chat.clients;

import ru.simple.chat.models.User;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * The interface User client.
 */
public interface UserClient {
    /**
     * Gets all users.
     *
     * @return the all users
     */
    List<User> getAllUsers();

    /**
     * Create new user.
     *
     * @param nick of new user
     * @return created user
     * @throws IllegalArgumentException the illegal argument exception
     */
    User createUser(String nick) throws IllegalArgumentException;

    /**
     * Delete user by nick.
     *
     * @param nick of deleted user
     * @return has been user deleted
     * @throws NoSuchElementException the no such element exception
     */
    boolean deleteUser(String nick) throws NoSuchElementException;

    /**
     * Gets User by nick.
     *
     * @param nick
     * @return User by nick
     * @throws NoSuchElementException the no such element exception
     */
    User getByNick(String nick) throws NoSuchElementException;

    /**
     * Contains by nick.
     *
     * @param nick the nick
     * @return the boolean
     */
    boolean contains(String nick);
}
