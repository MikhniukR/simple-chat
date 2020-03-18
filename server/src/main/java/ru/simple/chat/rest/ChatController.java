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
import ru.simple.chat.clients.UserClient;
import ru.simple.chat.models.Chat;
import ru.simple.chat.models.User;

import java.util.List;
import java.util.Optional;

/**
 * The base Chat controller.
 */
@RestController
public class ChatController {

    private final ChatClient chatClient;
    private final UserClient userClient;

    /**
     * Instantiates a new Chat controller.
     *
     * @param chatClient the chat client
     * @param userClient the user client
     */
    @Autowired
    ChatController(ChatClient chatClient, UserClient userClient) {
        this.chatClient = chatClient;
        this.userClient = userClient;
    }

    /**
     * Gets all chats.
     *
     * @return the all chats
     */
    @GetMapping("/chat")
    public ResponseEntity<List<Chat>> getAllChats() {
        return new ResponseEntity<>(chatClient.getAllChats(), HttpStatus.OK);
    }

    /**
     * Create chat chat.
     *
     * @param admin        the admin
     * @param chatName     the name
     * @param participants the participants
     * @return the chat
     */
    @PostMapping("/chat")
    public ResponseEntity<?> createChat(@RequestParam(value = "admin") User admin,
                                        @RequestParam(value = "name") String chatName,
                                        @RequestParam(value = "participants") List<User> participants) {

        if (chatClient.contains(chatName)) {
            return new ResponseEntity<>("Chat with name " + chatName + " already exists",
                    HttpStatus.BAD_REQUEST);
        }
        //TODO think about the faster variant this or userClient.contains() for every participant
        List<User> users = userClient.getAllUsers();
        Optional<User> badUser = participants.stream().filter(user -> !users.contains(user)).findFirst();
        if (badUser.isPresent()) {
            return new ResponseEntity<>("User with nick " + badUser.get().getNick() + " not exists",
                    HttpStatus.BAD_REQUEST);
        }
        if(!participants.contains(admin)) {
            return new ResponseEntity<>("Admin should be in participants", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(chatClient.createChat(admin, chatName, participants), HttpStatus.OK);
    }

    /**
     * Delete chat boolean.
     *
     * @param chatName the name
     * @param author   the author
     * @return the boolean
     */
    @DeleteMapping("/chat/{name}")
    public ResponseEntity<?> deleteChat(@PathVariable("name") String chatName,
                                     @RequestParam(value = "user") User author) {
        if (!chatClient.contains(chatName)) {
            return new ResponseEntity<>("No chat with name " + chatName, HttpStatus.BAD_REQUEST);
        }

        if (!chatClient.getChatByName(chatName).getAdmin().equals(author)) {
            return new ResponseEntity<>("Only admin can delete chat", HttpStatus.BAD_REQUEST);
        }

        chatClient.deleteChat(chatName);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<?> addMessage(@PathVariable("chatName") String chatName,
                                        @RequestParam(value = "author") User author,
                                        @RequestParam(value = "text") String text) {
        if (!userClient.contains(author.getNick())) {
            return new ResponseEntity<>("User with nick " + author.getNick() + " not exists",
                    HttpStatus.BAD_REQUEST);
        }

        if (!chatClient.contains(chatName)) {
            return new ResponseEntity<>("No chat with name " + chatName, HttpStatus.BAD_REQUEST);
        }

        try {
            return new ResponseEntity<>(chatClient.addMessage(chatName, author, text), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Gets chat by name
     *
     * @param chatName name of chat
     * @return the chat
     */
    @GetMapping("/chat/{chatName}")
    public ResponseEntity<?> getChat(@PathVariable("chatName") String chatName,
                                     @RequestParam(value = "author") User author) {
        if (!chatClient.contains(chatName)) {
            return new ResponseEntity<>("No chat with name " + chatName, HttpStatus.BAD_REQUEST);
        }

        if (!userClient.contains(author.getNick())) {
            return new ResponseEntity<>("User with nick " + author.getNick() + " not exists",
                    HttpStatus.BAD_REQUEST);
        }

        Chat chat = chatClient.getChatByName(chatName);

        if (!chat.getParticipants().contains(author)) {
            return new ResponseEntity<>("You are not participant of chat " + chatName, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    /**
     * Gets messages by chatName.
     *
     * @param chatName the chat name
     * @param author   the author
     * @return the messages
     */
    @GetMapping("/chat/{chatName}/messages")
    public ResponseEntity<?> getMessages(@PathVariable("chatName") String chatName,
                                         @RequestParam(value = "author") User author) {
        ResponseEntity<?> response = getChat(chatName, author);
        if (!response.getStatusCode().equals(HttpStatus.OK)) {
            return response;
        }
        Chat chat = (Chat) response.getBody();
        if (chat != null) {
            return new ResponseEntity<>(chat.getMessages(), HttpStatus.OK);
        }
        return response;
    }
}
