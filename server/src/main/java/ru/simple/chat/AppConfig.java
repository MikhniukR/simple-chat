package ru.simple.chat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.simple.chat.clients.ChatClient;
import ru.simple.chat.clients.ChatClientInMemory;
import ru.simple.chat.clients.UserClient;
import ru.simple.chat.clients.UserClientInMemory;

@Configuration
public class AppConfig {

    @Bean
    public ChatClient chatClient() {
        return new ChatClientInMemory();
    }

    @Bean
    public UserClient userClient() {
        return new UserClientInMemory();
    }
}
