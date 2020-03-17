package ru.simple.chat.clients;

import org.junit.Assert;
import org.junit.Test;
import ru.simple.chat.models.User;

import static org.junit.Assert.*;

public class UserClientInMemoryTest {

    @Test
    public void testGetAllUsers() {
        UserClient client = new UserClientInMemory();
        assertEquals(0, client.getAllUsers().size());

        client.createUser("test");
        assertEquals(1, client.getAllUsers().size());
        Assert.assertTrue(client.getAllUsers().contains(new User("test")));
    }

    @Test
    public void testDeleteUser() {
        UserClient client = new UserClientInMemory();
        client.createUser("test");
        assertEquals(1, client.getAllUsers().size());

        client.deleteUser("test");
        Assert.assertTrue(client.getAllUsers().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnmodifiable() {
        UserClient client = new UserClientInMemory();
        client.createUser("test");

        client.getAllUsers().add(new User("fail"));
        assertEquals(1, client.getAllUsers().size());
    }

    @Test
    public void testGetByNick() {
        UserClient client = new UserClientInMemory();
        client.createUser("test");
        client.createUser("other");

        Assert.assertEquals(client.getByNick("test"), new User("test"));
    }

    @Test
    public void testContains() {
        UserClient client = new UserClientInMemory();
        client.createUser("test");
        client.createUser("other");

        Assert.assertTrue(client.contains("test"));
    }
}