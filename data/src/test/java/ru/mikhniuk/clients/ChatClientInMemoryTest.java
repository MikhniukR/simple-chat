package ru.mikhniuk.clients;

import org.junit.Assert;
import org.junit.Test;
import ru.mikhniuk.models.Chat;
import ru.mikhniuk.models.Message;
import ru.mikhniuk.models.User;

import java.util.List;

import static org.junit.Assert.*;

public class ChatClientInMemoryTest {

    @Test
    public void testGetAllChats() {
        ChatClient client = new ChatClientInMemory();

        User admin1 = new User("admin");
        User participant1 = new User("participant");
        User admin2 = new User("root");
        User participant2 = new User("otherParticipant");

        client.createChat(admin1, "test", List.of(participant1, admin1));
        client.createChat(admin2, "otherChat", List.of(participant1, participant2, admin2));

        List<Chat> chats = client.getAllChats();
        Assert.assertEquals(2, chats.size());
        Assert.assertTrue(chats.contains(new Chat(admin1, "test", List.of(participant1, admin1))));
        Assert.assertTrue(chats.contains(new Chat(admin2, "otherChat", List.of(participant1, participant2, admin2))));
    }

    @Test
    public void testRemoveChat() {
        ChatClient client = new ChatClientInMemory();

        User admin = new User("admin");
        User participant = new User("participant");

        client.createChat(admin, "test", List.of(participant, admin));
        Assert.assertEquals(1, client.getAllChats().size());

        client.removeChat("test");
        Assert.assertTrue(client.getAllChats().isEmpty());
    }

    @Test
    public void testAddMessage() {
        ChatClient client = new ChatClientInMemory();

        User admin = new User("admin");
        User participant = new User("participant");

        client.createChat(admin, "test", List.of(participant, admin));
        client.addMessage("test", admin, "some message");
        client.addMessage("test", participant, "some other message");

        Chat chat = client.getAllChats().get(0);
        Assert.assertEquals(2, chat.getMessages().size());
        Assert.assertTrue(chat.getMessages().stream().anyMatch(
                message -> message.getText().equals("some message") &&
                        message.getAuthor().equals(admin)
        ));
        Assert.assertTrue(chat.getMessages().stream().anyMatch(
                message -> message.getText().equals("some other message") &&
                        message.getAuthor().equals(participant)
        ));
    }

    @Test
    public void testUnmodifiableAddChat() {
        ChatClient client = new ChatClientInMemory();

        User admin = new User("admin");
        User participant = new User("participant");
        client.createChat(admin, "test", List.of(participant, admin));

        client.getAllChats().add(new Chat(admin, "other chat", List.of(admin)));
        Assert.assertEquals(client.getAllChats().size(), 1);

        Chat chat = client.getAllChats().get(0);
        chat.addMessage(new Message(admin, "try add message"));
        Assert.assertEquals(0, client.getAllChats().get(0).getMessages().size());
    }

    @Test
    public void testUnmodifiableAddMessage() {
        ChatClient client = new ChatClientInMemory();

        User admin = new User("admin");
        User participant = new User("participant");
        client.createChat(admin, "test", List.of(participant, admin));

        Chat chat = client.getAllChats().get(0);
        chat.addMessage(new Message(admin, "try add message"));
        Assert.assertEquals(0, client.getAllChats().get(0).getMessages().size());
    }

    @Test
    public void testUnmodifiableAddParticipant() {
        ChatClient client = new ChatClientInMemory();

        User admin = new User("admin");
        User participant = new User("participant");
        client.createChat(admin, "test", List.of(admin));

        Chat chat = client.getAllChats().get(0);
        chat.getParticipants().add(participant);
        Assert.assertEquals(1, client.getAllChats().get(0).getParticipants().size());
    }
}