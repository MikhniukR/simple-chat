package ru.simple.chat.rest;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.simple.chat.clients.ChatClient;
import ru.simple.chat.clients.UserClient;
import ru.simple.chat.models.Chat;
import ru.simple.chat.models.Message;
import ru.simple.chat.models.User;

import java.util.List;

import static org.mockito.Mockito.when;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChatControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ChatClient chatClient;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private ChatController chatController;

    private final User admin = new User("admin");
    private final User user = new User("user");
    private final Chat chat = new Chat(admin, "chat", List.of(admin, user));

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
    }

    void initSomeData() {
        when(userClient.getAllUsers()).thenReturn(List.of(admin, user));
        when(userClient.contains(admin.getNick())).thenReturn(true);
        when(userClient.contains(user.getNick())).thenReturn(true);
        when(userClient.getByNick(admin.getNick())).thenReturn(admin);
        when(userClient.getByNick(user.getNick())).thenReturn(user);

        when(chatClient.getAllChats()).thenReturn(List.of(chat));
        when(chatClient.contains(chat.getName())).thenReturn(true);
        when(chatClient.getChatByName(chat.getName())).thenReturn(chat);
    }

    @Test
    void getAllChats() throws Exception {
        initSomeData();

        mockMvc.perform(
                get("/chat")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(chat.getName()))
                .andExpect(jsonPath("$[0].participants", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].admin.nick").value(admin.getNick()));

        verify(chatClient).getAllChats();
        verifyNoMoreInteractions(chatClient);
    }

    @Test
    void createChat() throws Exception {
        when(chatClient.contains(chat.getName())).thenReturn(false);
        when(userClient.getAllUsers()).thenReturn(List.of(admin, user));
        when(chatClient.createChat(chat.getAdmin(), chat.getName(), chat.getParticipants())).thenReturn(chat);

        mockMvc.perform(
                post("/chat")
                        .param("admin", admin.getNick())
                        .param("name", chat.getName())
                        .param("participants", admin.getNick() + ", " + user.getNick())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("admin.nick").value(admin.getNick()))
                .andExpect(jsonPath("name").value(chat.getName()))
                .andExpect(jsonPath("participants", Matchers.hasSize(2)));

        verify(chatClient).contains(chat.getName());
        verify(userClient).getAllUsers();
        verify(chatClient).createChat(chat.getAdmin(), chat.getName(), chat.getParticipants());
        verifyNoMoreInteractions(chatClient);
        verifyNoMoreInteractions(userClient);
    }

    @Test
    void createExistChat() throws Exception {
        when(chatClient.contains(chat.getName())).thenReturn(true);

        mockMvc.perform(
                post("/chat")
                        .param("admin", admin.getNick())
                        .param("name", chat.getName())
                        .param("participants", admin.getNick() + ", " + user.getNick())
        ).andExpect(status().isBadRequest());

        verify(chatClient).contains(chat.getName());
        verifyNoMoreInteractions(chatClient);
        verifyNoMoreInteractions(userClient);
    }

    @Test
    void createChatWithBadParticipants() throws Exception {
        when(chatClient.contains(chat.getName())).thenReturn(false);
        when(userClient.getAllUsers()).thenReturn(List.of(admin, user));

        mockMvc.perform(
                post("/chat")
                        .param("admin", admin.getNick())
                        .param("name", chat.getName())
                        .param("participants", user.getNick())
        ).andExpect(status().isBadRequest());

        verify(userClient).getAllUsers();
        verify(chatClient).contains(chat.getName());
        verifyNoMoreInteractions(chatClient);
        verifyNoMoreInteractions(userClient);
    }

    @Test
    void deleteChat() throws Exception {
        when(chatClient.contains(chat.getName())).thenReturn(true);
        when(chatClient.getChatByName(chat.getName())).thenReturn(chat);

        mockMvc.perform(
                delete("/chat/" + chat.getName())
                        .param("user", admin.getNick())
        ).andExpect(status().isOk());

        verify(chatClient).contains(chat.getName());
        verify(chatClient).getChatByName(chat.getName());
        verify(chatClient).deleteChat(chat.getName());
        verifyNoMoreInteractions(chatClient);
        verifyNoMoreInteractions(userClient);
    }

    @Test
    void deleteNotChat() throws Exception {
        when(chatClient.contains(chat.getName())).thenReturn(false);

        mockMvc.perform(
                delete("/chat/" + chat.getName())
                        .param("user", admin.getNick())
        ).andExpect(status().isBadRequest());

        verify(chatClient).contains(chat.getName());
        verifyNoMoreInteractions(chatClient);
        verifyNoMoreInteractions(userClient);
    }

    @Test
    void deleteChatByNotAdmin() throws Exception {
        when(chatClient.contains(chat.getName())).thenReturn(true);
        when(chatClient.getChatByName(chat.getName())).thenReturn(chat);

        mockMvc.perform(
                delete("/chat/" + chat.getName())
                        .param("user", user.getNick())
        ).andExpect(status().isBadRequest());

        verify(chatClient).contains(chat.getName());
        verify(chatClient).getChatByName(chat.getName());
        verifyNoMoreInteractions(chatClient);
        verifyNoMoreInteractions(userClient);
    }

    @Test
    void addMessage() throws Exception {
        when(userClient.contains(user.getNick())).thenReturn(true);
        when(chatClient.contains(chat.getName())).thenReturn(true);
        when(chatClient.addMessage(chat.getName(), user, "some message")).
                thenReturn(new Message(user, "some message"));

        mockMvc.perform(
                post("/chat/" + chat.getName())
                        .param("author", user.getNick())
                        .param("text", "some message")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("text").value("some message"));

        verify(userClient).contains(user.getNick());
        verify(chatClient).contains(chat.getName());
        verify(chatClient).addMessage(chat.getName(), user, "some message");
        verifyNoMoreInteractions(userClient);
        verifyNoMoreInteractions(chatClient);
    }

    @Test
    void addMessageInvalidUser() throws Exception {
        when(userClient.contains(user.getNick())).thenReturn(false);
        when(chatClient.addMessage(chat.getName(), user, "some message")).
                thenReturn(new Message(user, "some message"));

        mockMvc.perform(
                post("/chat/" + chat.getName())
                        .param("author", user.getNick())
                        .param("text", "some message")
        ).andExpect(status().isBadRequest());

        verify(userClient).contains(user.getNick());
        verifyNoMoreInteractions(userClient);
        verifyNoMoreInteractions(chatClient);
    }

    @Test
    void addMessageInvalidChat() throws Exception {
        when(userClient.contains(user.getNick())).thenReturn(true);
        when(chatClient.contains(chat.getName())).thenReturn(false);

        mockMvc.perform(
                post("/chat/" + chat.getName())
                        .param("author", user.getNick())
                        .param("text", "some message")
        ).andExpect(status().isBadRequest());

        verify(userClient).contains(user.getNick());
        verify(chatClient).contains(chat.getName());
        verifyNoMoreInteractions(userClient);
        verifyNoMoreInteractions(chatClient);
    }

    @Test
    void addMessageNotParticipant() throws Exception {
        when(userClient.contains(user.getNick())).thenReturn(true);
        when(chatClient.contains(chat.getName())).thenReturn(true);
        when(chatClient.addMessage(chat.getName(), user, "some message")).
                thenThrow(IllegalArgumentException.class);

        mockMvc.perform(
                post("/chat/" + chat.getName())
                        .param("author", user.getNick())
                        .param("text", "some message")
        ).andExpect(status().isBadRequest());

        verify(userClient).contains(user.getNick());
        verify(chatClient).contains(chat.getName());
        verify(chatClient).addMessage(chat.getName(), user, "some message");
        verifyNoMoreInteractions(userClient);
        verifyNoMoreInteractions(chatClient);
    }

    @Test
    void getChat() throws Exception {
        when(chatClient.contains(chat.getName())).thenReturn(true);
        when(userClient.contains(user.getNick())).thenReturn(true);
        when(chatClient.getChatByName(chat.getName())).thenReturn(chat);

        mockMvc.perform(
                get("/chat/" + chat.getName())
                        .param("author", user.getNick())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("name").value(chat.getName()));

        verify(chatClient).contains(chat.getName());
        verify(userClient).contains(user.getNick());
        verify(chatClient).getChatByName(chat.getName());
        verifyNoMoreInteractions(userClient);
        verifyNoMoreInteractions(chatClient);
    }

    @Test
    void getChatInvalidChatName() throws Exception {
        when(chatClient.contains(chat.getName())).thenReturn(false);

        mockMvc.perform(
                get("/chat/" + chat.getName())
                        .param("author", user.getNick())
        ).andExpect(status().isBadRequest());

        verify(chatClient).contains(chat.getName());
        verifyNoMoreInteractions(userClient);
        verifyNoMoreInteractions(chatClient);
    }

    @Test
    void getChatNotExistUser() throws Exception {
        when(chatClient.contains(chat.getName())).thenReturn(true);
        when(userClient.contains(user.getNick())).thenReturn(false);

        mockMvc.perform(
                get("/chat/" + chat.getName())
                        .param("author", user.getNick())
        ).andExpect(status().isBadRequest());

        verify(chatClient).contains(chat.getName());
        verify(userClient).contains(user.getNick());
        verifyNoMoreInteractions(userClient);
        verifyNoMoreInteractions(chatClient);
    }

    @Test
    void getChatInvalidUser() throws Exception {
        when(chatClient.contains(chat.getName())).thenReturn(true);
        when(userClient.contains("otherUser")).thenReturn(false);

        mockMvc.perform(
                get("/chat/" + chat.getName())
                        .param("author", "otherUser")
        ).andExpect(status().isBadRequest());

        verify(chatClient).contains(chat.getName());
        verify(userClient).contains("otherUser");
        verifyNoMoreInteractions(userClient);
        verifyNoMoreInteractions(chatClient);
    }

    @Test
    void getMessages() throws Exception {
        when(chatClient.contains(chat.getName())).thenReturn(true);
        when(userClient.contains(user.getNick())).thenReturn(true);
        when(chatClient.getChatByName(chat.getName())).thenReturn(chat);

        mockMvc.perform(
                get("/chat/" + chat.getName() + "/messages")
                        .param("author", user.getNick())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));

        verify(chatClient).contains(chat.getName());
        verify(userClient).contains(user.getNick());
        verify(chatClient).getChatByName(chat.getName());
        verifyNoMoreInteractions(userClient);
        verifyNoMoreInteractions(chatClient);

    }
}