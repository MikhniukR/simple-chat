package ru.mikhniuk.models;

import java.util.Objects;

public class User {
    private final String nick;

    public User(String nick) {
        this.nick = nick;
    }

    public User(User user) {
        this.nick = user.nick;
    }

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
