package ru.simple.chat.rest;

import com.sun.tools.javac.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.simple.chat.clients.UserClient;
import ru.simple.chat.models.User;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void createUser() throws Exception {
        User user = new User("nick");
        when(userClient.createUser(user.getNick())).thenReturn(user);
        when(userClient.contains("nick")).thenReturn(false);

        mockMvc.perform(
                post("/user")
                        .param("nick", user.getNick())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("nick").value(user.getNick()));

        verify(userClient).createUser(user.getNick());
        verify(userClient).contains(user.getNick());
        verifyNoMoreInteractions(userClient);
    }

    @Test
    void createExistUser() throws Exception {
        User user = new User("nick");
        when(userClient.contains("nick")).thenReturn(true);

        mockMvc.perform(
                post("/user")
                        .param("nick", user.getNick())
        ).andExpect(status().isBadRequest());

        verify(userClient).contains(user.getNick());
        verifyNoMoreInteractions(userClient);
    }

    @Test
    void getAllUsers() throws Exception {
        User user1 = new User("user1");
        User user2 = new User("user2");
        User user3 = new User("user3");
        when(userClient.getAllUsers()).thenReturn(List.of(user1, user2, user3));

        mockMvc.perform(
                get("/user")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));

        verify(userClient).getAllUsers();
        verifyNoMoreInteractions(userClient);
    }

    @Test
    void deleteExistUser() throws Exception {
        User user = new User("nick");
        when(userClient.contains(user.getNick())).thenReturn(true);

        mockMvc.perform(
                delete("/user/" + user.getNick())
        ).andExpect(status().isOk());

        verify(userClient).contains(user.getNick());
        verify(userClient).deleteUser(user.getNick());
        verifyNoMoreInteractions(userClient);
    }

    @Test
    void deleteNotExistUser() throws Exception {
        User user = new User("nick");
        when(userClient.contains(user.getNick())).thenReturn(false);

        mockMvc.perform(
                delete("/user/" + user.getNick())
        ).andExpect(status().isBadRequest());

        verify(userClient).contains(user.getNick());
        verifyNoMoreInteractions(userClient);
    }

    @Test
    void getExistUserByNick() throws Exception {
        User user = new User("nick");
        when(userClient.contains(user.getNick())).thenReturn(true);
        when(userClient.getByNick(user.getNick())).thenReturn(user);

        mockMvc.perform(
                get("/user/" + user.getNick())
        ).andExpect(status().isOk())
                .andExpect(jsonPath("nick").value(user.getNick()));

        verify(userClient).contains(user.getNick());
        verify(userClient).getByNick(user.getNick());
        verifyNoMoreInteractions(userClient);
    }

    @Test
    void getNotExistUserByNick() throws Exception {
        User user = new User("nick");
        when(userClient.contains(user.getNick())).thenReturn(false);

        mockMvc.perform(
                get("/user/" + user.getNick())
        ).andExpect(status().isBadRequest());

        verify(userClient).contains(user.getNick());
        verifyNoMoreInteractions(userClient);
    }
}