package ru.mikhniuk.clients;

import org.junit.Assert;
import org.junit.Test;
import ru.mikhniuk.models.User;

import static org.junit.Assert.*;

public class UserClientInMemoryTest {

    @Test
    public void testGetAllUsers() {
        UserClient client = new UserClientInMemory();
        Assert.assertEquals(0, client.getAllUsers().size());

        client.createUser("test");
        Assert.assertEquals(1, client.getAllUsers().size());
        Assert.assertTrue(client.getAllUsers().contains(new User("test")));
    }

    @Test
    public void testDeleteUser() {
        UserClient client = new UserClientInMemory();
        client.createUser("test");
        Assert.assertEquals(1, client.getAllUsers().size());

        client.deleteUser("test");
        Assert.assertTrue(client.getAllUsers().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnmodifiable() {
        UserClient client = new UserClientInMemory();
        client.createUser("test");

        client.getAllUsers().add(new User("fail"));
        Assert.assertEquals(1, client.getAllUsers().size());
    }
}