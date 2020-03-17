package ru.simple.chat.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.simple.chat.clients.UserClient;
import ru.simple.chat.clients.UserClientInMemory;
import ru.simple.chat.models.User;

import java.util.List;

@RestController
public class UserController {

    private UserClient userClient;

    @Autowired
    UserController(UserClient client) {
        this.userClient = client;
    }

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return userClient.getAllUsers();
    }

    @PostMapping("/user")
    public User createUser(@RequestParam(value = "nick") String nick) {
        return userClient.createUser(nick);
    }

    @DeleteMapping("/user/{nick}")
    public boolean deleteUser(@PathVariable("nick")String nick) {
        return userClient.deleteUser(nick);
    }
}
