package ru.simple.chat.clients;

import ru.simple.chat.models.Message;
import ru.simple.chat.models.User;
import ru.simple.chat.models.Chat;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The type Chat client in memory.
 */
public class ChatClientInMemory implements ChatClient {
    private final List<Chat> chats;

    /**
     * Instantiates a new Chat client in memory.
     */
    public ChatClientInMemory() {
        this.chats = new LinkedList<>();
    }

    @Override
    public List<Chat> getAllChats() {
        List<Chat> result = new LinkedList<>();
        chats.forEach(chat -> result.add(new Chat(chat)));

        return result;
    }

    @Override
    public Chat createChat(User admin, String chatName, List<User> participants) throws IllegalArgumentException {
        if (contains(chatName)) {
            throw new IllegalArgumentException("Chat with name " + chatName + " already exists, chat name should be uniq");
        }

        if (!participants.contains(admin)) {
            throw new IllegalArgumentException("Admin should be in participants.");
        }

        Chat chat = new Chat(admin, chatName, participants);
        chats.add(chat);

        return new Chat(chat);
    }

    @Override
    public void deleteChat(String chatName) throws NoSuchElementException {
        if (!contains(chatName)) {
            throw new NoSuchElementException("There isn't chat with name " + chatName);
        }

        chats.removeIf(
                chat -> chat.getName().equals(chatName)
        );
    }

    @Override
    public Message addMessage(String chatName, User author, String text)
            throws NoSuchElementException, IllegalArgumentException {
        if (!contains(chatName)) {
            throw new NoSuchElementException("There isn't chat with name " + chatName);
        }
        //No isPresent because it checked with contains
        Chat chat = chats.stream().filter(c -> c.getName().equals(chatName))
                .findFirst().get();
        if (chat.getParticipants().stream().noneMatch(
                user -> user.getNick().equals(author.getNick()))) {
            throw new IllegalArgumentException("User " + author.getNick() + " has no access to this chat");
        }

        Message message = new Message(author, text);
        chats.stream().filter(
                c -> c.getName().equals(chatName)
        ).forEach(
                c -> c.addMessage(new Message(author, text))
        );

        return message;
    }

    @Override
    public Chat getChatByName(String chatName) {
        if (!contains(chatName)) {
            throw new NoSuchElementException("There isn't chat with name " + chatName);
        }

        return new Chat(chats.stream().filter(
                chat -> chat.getName().equals(chatName))
                .findFirst().get());
    }

    @Override
    public boolean contains(String chatName) {
        return chats.stream().anyMatch(
                chat -> chat.getName().equals(chatName));
    }
}
