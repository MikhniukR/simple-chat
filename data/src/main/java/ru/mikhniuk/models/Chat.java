package ru.mikhniuk.models;

import java.util.List;
import java.util.Objects;

public class Chat {
    private final User admin;
    private List<User> participants;
    private List<Message> messages;

    public Chat(User admin) {
        this.admin = admin;
    }

    public Chat(User admin, List<User> participants) {
        this.admin = admin;
        this.participants = participants;
    }

    public User getAdmin() {
        return admin;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chat)) return false;
        Chat chat = (Chat) o;
        return Objects.equals(getAdmin(), chat.getAdmin()) &&
                Objects.equals(getParticipants(), chat.getParticipants()) &&
                Objects.equals(getMessages(), chat.getMessages());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAdmin(), getParticipants(), getMessages());
    }

    @Override
    public String toString() {
        return "Chat{" +
                "admin=" + admin +
                ", participants=" + participants +
                ", messages=" + messages +
                '}';
    }
}