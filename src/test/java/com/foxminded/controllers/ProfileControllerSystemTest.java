package com.foxminded.controllers;

import com.foxminded.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class ProfileControllerSystemTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testProfilePage_UserSignedIn_ShouldShowProfilePage() throws Exception {
        UserDto user = new UserDto("1", "student", "name", "surname", "about");
        HttpSession session = this.mockMvc.perform(get("/profile")
                .sessionAttr("user", user))
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
        UserDto user = new UserDto("2", "professor", "name", "surname", "a");
        this.mockMvc.perform(post("/profile/save")
                .flashAttr("user", user))
                .andExpect(redirectedUrl("/profile"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isFound());
    }

    @Test
    void testSaveUser_InvalidInput_ShouldReturnProfilePage() throws Exception {
        this.mockMvc.perform(post("/profile/save")
                .flashAttr("user", new UserDto()))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("error"));
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
    }

    @Test
    void testDeleteUser_InvalidInput_ShouldReturnProfilePage() throws Exception {
        this.mockMvc.perform(post("/profile/delete")
                .flashAttr("user", new UserDto()))
                .andExpect(status().isBadRequest());
    }
}
