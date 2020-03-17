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

/**
 * The base User controller.
 */
@RestController
public class UserController {

    private final UserClient userClient;

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
    public ResponseEntity<?> createUser(@RequestParam(value = "nick") String nick) {
        if (userClient.contains(nick)) {
            return new ResponseEntity<>("User with nick " + nick + " already exists",
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userClient.createUser(nick), HttpStatus.OK);
    }

    /**
     * Delete user boolean.
     *
     * @param nick the nick
     * @return the boolean
     */
    @DeleteMapping("/user/{nick}")
    public ResponseEntity<?> deleteUser(@PathVariable("nick") String nick) {
        if (!userClient.contains(nick)) {
            return new ResponseEntity<>("User with nick " + nick + " not exists",
                    HttpStatus.BAD_REQUEST);
        }

        userClient.deleteUser(nick);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Gets user by nick.
     *
     * @param nick the nick
     * @return the user by nick
     */
    @GetMapping("/user/{nick}")
    public ResponseEntity<?> getUserByNick(@PathVariable("nick") String nick) {
        if (!userClient.contains(nick)) {
            return new ResponseEntity<>("User with nick " + nick + " not exists",
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userClient.getByNick(nick), HttpStatus.OK);
    }
}
