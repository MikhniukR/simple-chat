package ru.mikhniuk.models;

import java.util.Objects;

/**
 * The type User.
 * Contains uniq user nick
 */
public class User {
    private final String nick;

    /**
     * Instantiates a new User.
     *
     * @param nick the nick
     */
    public User(String nick) {
        this.nick = nick;
    }

    /**
     * Instantiates a new User.
     *
     * @param user the user
     */
    public User(User user) {
        this.nick = user.nick;
    }

    /**
     * Gets nick.
     *
     * @return the nick
     */
    public String getNick() {
        return nick;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getNick(), user.getNick());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNick());
    }

    @Override
    public String toString() {
        return "User{" +
                "nick='" + nick + '\'' +
                '}';
    }
}
