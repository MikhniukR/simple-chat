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
import ru.simple.chat.clients.ChatClient;
import ru.simple.chat.models.Chat;
import ru.simple.chat.models.Message;
import ru.simple.chat.models.User;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * The base Chat controller.
 */
@RestController
public class ChatController {

    private ChatClient chatClient;

    /**
     * Instantiates a new Chat controller.
     *
     * @param client the client
     */
    @Autowired
    ChatController(ChatClient client) {
        this.chatClient = client;
    }

    /**
     * Gets all chats.
     *
     * @return the all chats
     */
    @GetMapping("/chat")
    public ResponseEntity<List<Chat>> getAllChats() {
        return new ResponseEntity(chatClient.getAllChats(), HttpStatus.OK);
    }

    /**
     * Create chat chat.
     *
     * @param admin        the admin
     * @param name         the name
     * @param participants the participants
     * @return the chat
     */
    @PostMapping("/chat")
    public ResponseEntity<Chat> createChat(@RequestParam(value = "admin") User admin,
                           @RequestParam(value = "name") String name,
                           @RequestParam(value = "participants") List<User> participants) {
        try {
            return new ResponseEntity(chatClient.createChat(admin, name, participants), HttpStatus.OK);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete chat boolean.
     *
     * @param name   the name
     * @param author the author
     * @return the boolean
     */
    @DeleteMapping("/chat/{name}")
    public ResponseEntity deleteChat(@PathVariable("name")String name,
                              @RequestParam(value = "user")  User author) {
        try {
            chatClient.removeChat(name);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Add message message.
     *
     * @param chatName the chat name
     * @param author   the author
     * @param text     the text
     * @return the message
     */
    @PostMapping("/chat/{chatName}")
    public ResponseEntity<Message> addMessage(@PathVariable("chatName")String chatName,
                              @RequestParam(value = "author") User author,
                              @RequestParam(value = "text") String text) {
        try {
            return new ResponseEntity<>(chatClient.addMessage(chatName, author, text), HttpStatus.OK);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
