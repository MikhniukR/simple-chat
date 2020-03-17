package ru.mikhniuk.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * The type Chat.
 * Contains all info about chat.
 * Name should be uniq for all chats.
 */
public class Chat {
    private final User admin;
    private final String name;
    private List<User> participants;
    private List<Message> messages;

    /**
     * Instantiates a new Chat.
     *
     * @param admin the admin
     * @param name  the name
     */
    public Chat(User admin, String name) {
        this.admin = admin;
        this.name = name;
    }

    /**
     * Instantiates a new Chat.
     *
     * @param admin        the admin
     * @param name         the name
     * @param participants the participants
     */
    public Chat(User admin, String name, List<User> participants) {
        this.admin = admin;
        this.name = name;
        this.participants = participants;
        this.messages = new LinkedList<>();
    }

    /**
     * Instantiates a new Chat.
     *
     * @param chat the chat
     */
    public Chat(Chat chat) {
        this.name = chat.name;
        this.participants = new LinkedList<>(chat.participants);
        this.messages = new LinkedList<>(chat.messages);
        this.admin = chat.admin;
    }

    /**
     * Gets admin.
     *
     * @return the admin
     */
    public User getAdmin() {
        return admin;
    }

    /**
     * Gets participants.
     *
     * @return the participants
     */
    public List<User> getParticipants() {
        return participants;
    }

    /**
     * Gets messages.
     *
     * @return the messages
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Add message.
     *
     * @param message the message to add
     */
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
