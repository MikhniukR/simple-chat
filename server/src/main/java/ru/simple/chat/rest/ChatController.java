package ru.simple.chat.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.simple.chat.clients.ChatClient;
import ru.simple.chat.clients.ChatClientInMemory;
import ru.simple.chat.models.Chat;
import ru.simple.chat.models.Message;
import ru.simple.chat.models.User;

import java.util.List;

@RestController
public class ChatController {

    private ChatClient chatClient;

    @Autowired
    ChatController(ChatClient client) {
        this.chatClient = client;
    }

    @GetMapping("/chat")
    public List<Chat> getAllChats() {
        return chatClient.getAllChats();
    }

    @PostMapping("/chat")
    public Chat createChat(@RequestParam(value = "admin") User admin,
                           @RequestParam(value = "name") String name,
                           @RequestParam(value = "participants") List<User> participants) {
        return chatClient.createChat(admin, name, participants);
    }

    @DeleteMapping("/chat/{name}")
    public boolean deleteChat(@PathVariable("name")String name,
                              @RequestParam(value = "user")  User author) {
        return chatClient.removeChat(name);
    }

    @PostMapping("/chat/{chatName}")
    public Message addMessage(@PathVariable("chatName")String chatName,
                              @RequestParam(value = "author") User author,
                              @RequestParam(value = "text") String text) {
        return chatClient.addMessage(chatName, author, text);
    }
}
