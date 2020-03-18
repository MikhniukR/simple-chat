package ru.simple.chat.clients;

import ru.simple.chat.models.Message;
import ru.simple.chat.models.User;
import ru.simple.chat.models.Chat;

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
     * @throws NoSuchElementException the no such element exception
     */
    void deleteChat(String name) throws NoSuchElementException;

    /**
     * Add new message to the chat
     *
     * @param chatName to add message
     * @param author   of new message
     * @param text     of new message
     */
    Message addMessage(String chatName, User author, String text)
            throws NoSuchElementException, IllegalArgumentException;

    /**
     * Gets chat by name.
     *
     * @param name of chat
     * @return chat by name
     */
    Chat getChatByName(String name) throws NoSuchElementException;

    /**
     * Contains by chat name.
     *
     * @param name the name
     * @return the boolean
     */
    boolean contains(String name);
}
