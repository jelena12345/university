package com.foxminded.controllers;

import com.foxminded.dto.AccountCredentials;
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
class IndexControllerSystemTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testGetIndexView_NotSignedIn_ShouldReturnIndexView() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testGetIndexView_SignedIn_ShouldRedirectToProfilePage() throws Exception {
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
        HttpSession session = this.mockMvc.perform(post("/signIn")
                .flashAttr("credentials", new AccountCredentials("1")))
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
        this.mockMvc.perform(post("/signIn")
                .flashAttr("credentials", new AccountCredentials("25")))
                .andExpect(view().name("index"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk());
    }

    @Test
    void testSignIn_InvalidInput_ShouldRedirectToIndexPage() throws Exception {
        this.mockMvc.perform(post("/signIn")
                .flashAttr("credentials", new AccountCredentials("")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegisterUser_Successful_ShouldRedirectToIndexPage() throws Exception {
        UserDto user = new UserDto("25", "student", "name", "surname", "a");
        this.mockMvc.perform(post("/register")
                .flashAttr("user", user))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().isFound());
    }

    @Test
    void testRegisterUser_UnsuccessfulWithValidData_ShouldReturnRegistrationPage() throws Exception {
        UserDto user = new UserDto("1", "student", "name", "surname", "a");
        this.mockMvc.perform(post("/register")
                .flashAttr("user", user))
                .andExpect(view().name("user/registration"))
                .andExpect(model().hasNoErrors())
                .andExpect(status().isOk());
    }

    @Test
    void testRegisterUser_UnsuccessfulWithInvalidData_ShouldReturnRegistrationPage() throws Exception {
        this.mockMvc.perform(post("/register")
                .flashAttr("user", new UserDto()))
                .andExpect(status().isBadRequest());
    }
}
