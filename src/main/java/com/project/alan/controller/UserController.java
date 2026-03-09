package com.project.alan.controller;

import com.project.alan.dto.UserInput;
import com.project.alan.model.User;
import com.project.alan.model.UserRole;
import com.project.alan.repositories.PersonRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "BasicAuth")
    private User createNewPerson(@RequestBody UserInput userInput) {
        String hashedPassword = passwordEncoder.encode(userInput.getPassword());
        User newUser = new User(userInput.getUsername(), hashedPassword, UserRole.USER);

        return personRepository.save(newUser);
    }
}
