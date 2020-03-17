package ru.mikhniuk.models;

import java.time.Instant;
import java.util.Objects;

/**
 * The type Message.
 */
public class Message {
    private final User author;
    private final String text;
    private final Instant time;

    /**
     * Instantiates a new Message.
     *
     * @param author the author
     * @param text   the text
     * @param time   the time
     */
    public Message(User author, String text, Instant time) {
        this.author = author;
        this.text = text;
        this.time = time;
    }

    /**
     * Instantiates a new Message.
     *
     * @param author the author nick
     * @param text   the text
     */
    public Message(User author, String text) {
        this.author = author;
        this.text = text;
        this.time = Instant.now();
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public Instant getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Objects.equals(getAuthor(), message.getAuthor()) &&
                Objects.equals(getText(), message.getText()) &&
                Objects.equals(getTime(), message.getTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthor(), getText(), getTime());
    }

    @Override
    public String toString() {
        return "Message{" +
                "authorNick=" + author +
                ", text='" + text + '\'' +
                ", time=" + time +
                '}';
    }
}
