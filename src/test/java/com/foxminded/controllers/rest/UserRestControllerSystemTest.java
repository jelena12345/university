package com.foxminded.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class UserRestControllerSystemTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testGetCourses_ShouldReturnCourses() throws Exception {
        this.mockMvc.perform(get("/rest/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].personalId").value("1"))
                .andExpect(jsonPath("$[0].role").value("student"))
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].surname").value("surname"))
                .andExpect(jsonPath("$[0].about").value("about"));
    }

    @Test
    void testGetUserByPersonalId_ShouldReturnUser() throws Exception {
        this.mockMvc.perform(get("/rest/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalId").value("1"))
                .andExpect(jsonPath("$.role").value("student"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.surname").value("surname"))
                .andExpect(jsonPath("$.about").value("about"));
    }

    @Test
    void testGetAvailableCourses_ShouldReturnCourses() throws Exception {
        this.mockMvc.perform(get("/rest/users/2/available")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("name"))
                .andExpect(jsonPath("$[0].description").value("description"));
    }

    @Test
    void testRegisterUser_ShouldRegisterUser() throws Exception {
        UserDto user = new UserDto("5", "student", "n", "s", "a");
        this.mockMvc.perform(post("/rest/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUser_ShouldUpdateUser() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        this.mockMvc.perform(post("/rest/users/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser_ShouldDeleteUser() throws Exception {
        UserDto user = new UserDto("1", "student", "n", "s", "a");
        this.mockMvc.perform(post("/rest/users/1/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
