package ru.mikhniuk.models;

import java.time.Instant;
import java.util.Objects;

public class Message {
    private final User author;
    private final String text;
    private final Instant time;

    public Message(User author, String text, Instant time) {
        this.author = author;
        this.text = text;
        this.time = time;
    }

    public Message(User author, String text) {
        this.author = author;
        this.text = text;
        this.time = Instant.now();
    }

    public User getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

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
                "author=" + author +
                ", text='" + text + '\'' +
                ", time=" + time +
                '}';
    }
}
