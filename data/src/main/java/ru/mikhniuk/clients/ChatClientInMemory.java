package ru.mikhniuk.clients;

import ru.mikhniuk.models.Chat;
import ru.mikhniuk.models.Message;
import ru.mikhniuk.models.User;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class ChatClientInMemory implements ChatClient {
    private List<Chat> chats;

    public ChatClientInMemory() {
        this.chats = new LinkedList<>();
    }

    @Override
    public List<Chat> getAllChats() {
        return Collections.unmodifiableList(chats);
    }

    @Override
    public Chat createChat(User admin, String name, List<User> participants) throws IllegalArgumentException {
        if(chats.stream().anyMatch(
                chat -> chat.getName().equals(name))) {
            throw new IllegalArgumentException();
        }

        Chat chat = new Chat(admin, name, participants);
        chats.add(chat);
        return new Chat(chat);
    }

    @Override
    public boolean removeChat(String name) throws NoSuchElementException {
        if(chats.stream().noneMatch(
                chat -> chat.getName().equals(name))) {
            throw new NoSuchElementException();
        }

        return chats.removeIf(
                chat -> chat.getName().equals(name)
        );
    }

    @Override
    public void addMessage(String chatName, User author, String text) throws NoSuchElementException {
        if(chats.stream().noneMatch(
                chat -> chat.getName().equals(chatName))) {
            throw new NoSuchElementException();
        }

        Message message = new Message(author, text);
        chats.stream().filter(
                chat -> chat.getName().equals(chatName)
        ).forEach(
                chat -> chat.addMessage(new Message(author, text))
        );
    }
}
