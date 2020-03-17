package ru.simple.chat.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.simple.chat.clients.UserClient;
import ru.simple.chat.models.User;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * The base User controller.
 */
@RestController
public class UserController {

    private UserClient userClient;

    /**
     * Instantiates a new User controller.
     *
     * @param client the client
     */
    @Autowired
    UserController(UserClient client) {
        this.userClient = client;
    }

    /**
     * Gets all users.
     *
     * @return the all users
     */
    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userClient.getAllUsers(), HttpStatus.OK);
    }

    /**
     * Create user user.
     *
     * @param nick the nick
     * @return the user
     */
    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestParam(value = "nick") String nick) {
        try {
            return new ResponseEntity<>(userClient.createUser(nick), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete user boolean.
     *
     * @param nick the nick
     * @return the boolean
     */
    @DeleteMapping("/user/{nick}")
    public ResponseEntity deleteUser(@PathVariable("nick") String nick) {
        try {
            userClient.deleteUser(nick);
            return new ResponseEntity(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
