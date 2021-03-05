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
    void testProfilePage_UserSignedIn_ShouldShowProfilePage() throws Exception {
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
    void testProfilePage_UserNotSignedIn_ShouldRedirectToIndexPage() throws Exception {
        this.mockMvc.perform(get("/profile"))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().isFound());
    }

    @Test
    void testSaveUser_ValidInput_ShouldRedirectToProfilePage() throws Exception {
        UserDto user = new UserDto("1", "student", "name", "surname", "a");
        this.mockMvc.perform(post("/profile/save")
                .flashAttr("user", user))
                .andExpect(redirectedUrl("/profile"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isFound());
        verify(userService, times(1)).save(user);
    }

    @Test
    void testSaveUser_InvalidInput_ShouldReturnProfilePage() throws Exception {
        this.mockMvc.perform(post("/profile/save")
                .flashAttr("user", new UserDto()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteUser_ValidInput_ShouldRedirectToProfilePage() throws Exception {
        UserDto user = new UserDto("1", "student", "name", "surname", "a");
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

    @Test
    void testDeleteUser_InvalidInput_ShouldReturnProfilePage() throws Exception {
        this.mockMvc.perform(post("/profile/delete")
                .flashAttr("user", new UserDto()))
                .andExpect(status().isBadRequest());
    }

}
