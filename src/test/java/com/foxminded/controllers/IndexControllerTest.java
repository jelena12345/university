package com.foxminded.controllers;

import com.foxminded.dto.UserDto;
import com.foxminded.services.UserService;
import com.foxminded.services.exceptions.EntityAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    private MockMvc mockMvc;
    @Mock
    private UserService userService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new IndexController(userService)).build();
    }

    @Test
    void testIndexPage_ShouldReturnIndexPage() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testIndexPage_ShouldRedirectToProfilePage() throws Exception {
        this.mockMvc.perform(get("/")
                .sessionAttr("user", new UserDto()))
                .andExpect(redirectedUrl("/profile"))
                .andExpect(status().isFound());
    }

    @Test
    void testRegistrationPage_ShouldReturnRegistrationPage() throws Exception {
        this.mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("user/registration"));
    }

    @Test
    void testSignOut_ShouldRedirectToIndexPage() throws Exception {
        HttpSession session = this.mockMvc.perform(get("/signOut")
                .sessionAttr("user", new UserDto()))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().isFound())
                .andReturn()
                .getRequest()
                .getSession();
        assertNotNull(session);
        assertNull(session.getAttribute("user"));
    }

    @Test
    void testSignIn_Successful_ShouldRedirectToProfilePage() throws Exception {
        when(userService.existsByPersonalId(anyString())).thenReturn(true);
        when(userService.findByPersonalId(anyString())).thenReturn(new UserDto());
        HttpSession session = this.mockMvc.perform(post("/signIn"))
                .andExpect(redirectedUrl("/profile"))
                .andExpect(status().isFound())
                .andReturn()
                .getRequest()
                .getSession();
        assertNotNull(session);
        assertNotNull(session.getAttribute("user"));
    }

    @Test
    void testSignIn_Unsuccessful_ShouldRedirectToIndexPage() throws Exception {
        this.mockMvc.perform(post("/signIn").flashAttr("personalId", "1"))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().isFound())
                .andExpect(flash().attributeExists("personalId"));
    }

    @Test
    void testRegisterUser_Successful_ShouldRedirectToIndexPage() throws Exception {
        UserDto user = new UserDto("1", "role", "name", "surname", "a");
        this.mockMvc.perform(post("/register")
                .flashAttr("user", user))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().isFound());
        verify(userService, times(1)).add(user);
    }

    @Test
    void testRegisterUser_Unsuccessful_ShouldRedirectToRegistrationPage() throws Exception {
        doThrow(new EntityAlreadyExistsException("")).when(userService).add(any());
        this.mockMvc.perform(post("/register")
                .flashAttr("user", new UserDto("1", "role", "name", "surname", "a")))
                .andExpect(redirectedUrl("/register"))
                .andExpect(status().isFound());
    }
}
