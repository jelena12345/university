package com.foxminded.controllers;

import com.foxminded.dto.AccountCredentials;
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
        when(userService.existsByPersonalId(anyString())).thenReturn(true);
        when(userService.findByPersonalId(anyString())).thenReturn(new UserDto());
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
                .flashAttr("credentials", new AccountCredentials("1")))
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
        UserDto user = new UserDto("1", "student", "name", "surname", "a");
        this.mockMvc.perform(post("/register")
                .flashAttr("user", user))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().isFound());
        verify(userService, times(1)).add(user);
    }

    @Test
    void testRegisterUser_UnsuccessfulWithValidData_ShouldReturnRegistrationPage() throws Exception {
        UserDto user = new UserDto("1", "student", "name", "surname", "a");
        doThrow(new EntityAlreadyExistsException("")).when(userService).add(user);
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
