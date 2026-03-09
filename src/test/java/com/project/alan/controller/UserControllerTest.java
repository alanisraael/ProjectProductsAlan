package com.project.alan.controller;

import com.project.alan.dto.UserInput;
import com.project.alan.model.User;
import com.project.alan.repositories.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UserController Tests")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        personRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /usuarios - Debe crear un nuevo usuario (autenticado)")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateNewPerson() throws Exception {
        UserInput userInput = new UserInput();
        userInput.setUsername("testuser");
        userInput.setPassword("password123");

        mockMvc.perform(post("/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userInput)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username", is("testuser")))
            .andExpect(jsonPath("$.role", is("USER")));
    }

    @Test
    @DisplayName("POST /usuarios - Debe encriptar la contraseña")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateNewPersonEncryptsPassword() throws Exception {
        UserInput userInput = new UserInput();
        userInput.setUsername("testuser2");
        userInput.setPassword("password123");

        mockMvc.perform(post("/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userInput)))
            .andExpect(status().isCreated());

        // Verificar que la contraseña esté encriptada
        User savedUser = personRepository.findByUsername("testuser2").orElse(null);
        assert savedUser != null;
        assert !savedUser.getPasswordHash().equals("password123"); // No debe ser igual a la contraseña plana
        assert passwordEncoder.matches("password123", savedUser.getPasswordHash()); // Debe coincidir con BCrypt
    }

    @Test
    @DisplayName("POST /usuarios - Debe asignar rol USER por defecto")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testCreateNewPersonAssignsUserRole() throws Exception {
        UserInput userInput = new UserInput();
        userInput.setUsername("testuser3");
        userInput.setPassword("password123");

        mockMvc.perform(post("/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userInput)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.role", is("USER")));
    }

}
