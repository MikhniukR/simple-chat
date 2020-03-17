package ru.mikhniuk.clients;

import ru.mikhniuk.models.Chat;
import ru.mikhniuk.models.User;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * The interface Chat client.
 */
public interface ChatClient {
    /**
     * Get list of all chats
     *
     * @return list of chats
     */

    List<Chat> getAllChats();

    /**
     * Create new chat
     *
     * @param admin        of new chat
     * @param name         of new chat, should be uniq
     * @param participants of new chats, should contains admin
     * @return copy of created Chat
     */
    Chat createChat(User admin, String name, List<User> participants)
            throws IllegalArgumentException;

    /**
     * Remove chat by name
     *
     * @param name of removed chat
     * @return has chat deleted
     */
    boolean removeChat(String name) throws NoSuchElementException;

    /**
     * Add new message to the chat
     *
     * @param chatName to add message
     * @param author   of new message
     * @param text     of new message
     */
    void addMessage(String chatName, User author, String text)
            throws NoSuchElementException, IllegalArgumentException;
}
