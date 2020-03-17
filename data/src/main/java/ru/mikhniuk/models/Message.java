package ru.mikhniuk.models;

import java.time.Instant;
import java.util.Objects;

public class Message {
    private final String authorNick;
    private final String text;
    private final Instant time;

    public Message(String authorNick, String text, Instant time) {
        this.authorNick = authorNick;
        this.text = text;
        this.time = time;
    }

    public Message(String authorNick, String text) {
        this.authorNick = authorNick;
        this.text = text;
        this.time = Instant.now();
    }

    public String getAuthor() {
        return authorNick;
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
                "authorNick=" + authorNick +
                ", text='" + text + '\'' +
                ", time=" + time +
                '}';
    }
}
