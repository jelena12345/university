package com.foxminded.controllers;

import com.foxminded.dto.UserDto;
import com.foxminded.services.UserService;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    private MockMvc mockMvc;
    @Mock
    private UserService userService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new ProfileController(userService)).build();
    }

    @Test
    void testProfilePage_UserLoggedIn_ShouldShowProfilePage() throws Exception {
        when(userService.findByPersonalId(anyString())).thenReturn(new UserDto());
        HttpSession session = this.mockMvc.perform(get("/profile")
                .sessionAttr("user", new UserDto("", "", "", "", "")))
                .andExpect(model().attributeExists("user"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile"))
                .andReturn()
                .getRequest()
                .getSession();
        assertNotNull(session);
        assertNotNull(session.getAttribute("user"));
    }

    @Test
    void testProfilePage_UserNotLoggedIn_ShouldRedirectToIndexPage() throws Exception {
        this.mockMvc.perform(get("/profile"))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().isFound());
    }

    @Test
    void testSaveUser_ShouldRedirectToProfilePage() throws Exception {
        UserDto user = new UserDto("1", "role", "name", "surname", "a");
        this.mockMvc.perform(post("/profile/save")
                .flashAttr("user", user))
                .andExpect(redirectedUrl("/profile"))
                .andExpect(status().isFound());
        verify(userService, times(1)).update(user);
    }

    @Test
    void testDeleteUser_ShouldRedirectToProfilePage() throws Exception {
        UserDto user = new UserDto("1", "role", "name", "surname", "a");
        HttpSession session = this.mockMvc.perform(post("/profile/delete")
                .flashAttr("user", user)
                .sessionAttr("user", user))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().isFound())
                .andReturn()
                .getRequest()
                .getSession();
        assertNotNull(session);
        assertNull(session.getAttribute("user"));
        verify(userService, times(1)).deleteByPersonalId(user.getPersonalId());
    }

}
