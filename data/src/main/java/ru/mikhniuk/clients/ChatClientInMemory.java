package ru.mikhniuk.clients;

import ru.mikhniuk.models.Chat;
import ru.mikhniuk.models.Message;
import ru.mikhniuk.models.User;

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
        List<Chat> result = new LinkedList<>();
        chats.forEach(chat -> result.add(new Chat(chat)));
        return result;
    }

    @Override
    public Chat createChat(User admin, String name, List<User> participants) throws IllegalArgumentException {
        if(chats.stream().anyMatch(
                chat -> chat.getName().equals(name))) {
            throw new IllegalArgumentException();
        }

        if(!participants.contains(admin)) {
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
    public void addMessage(String chatName, String authorNick, String text)
            throws NoSuchElementException, IllegalArgumentException {
        if(chats.stream().noneMatch(
                chat -> chat.getName().equals(chatName))) {
            throw new NoSuchElementException();
        }
        if(chats.stream().filter(
                chat -> chat.getName().equals(chatName))
                .findFirst().get().getParticipants().stream().noneMatch(
                        user -> user.getNick().equals(authorNick))) {
            throw new IllegalArgumentException();
        }

        Message message = new Message(authorNick, text);
        chats.stream().filter(
                chat -> chat.getName().equals(chatName)
        ).forEach(
                chat -> chat.addMessage(new Message(authorNick, text))
        );
    }
}
