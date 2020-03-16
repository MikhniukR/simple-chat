package ru.mikhniuk.clients;

import ru.mikhniuk.models.Chat;
import ru.mikhniuk.models.User;

import java.util.List;
import java.util.NoSuchElementException;

public interface ChatClient {
    List<Chat> getAllChats();
    Chat createChat(User admin, String name, List<User> participants) throws IllegalArgumentException;
    boolean removeChat(String name) throws NoSuchElementException;
    void addMessage(String chatName, User author, String text) throws NoSuchElementException;
}
