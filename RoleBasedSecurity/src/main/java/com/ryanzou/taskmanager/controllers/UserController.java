package com.ryanzou.taskmanager.controllers;

import com.ryanzou.taskmanager.beans.User;
import com.ryanzou.taskmanager.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = {"/api/admin"})
public class UserController {
    private UserRepository userRepository;

    @GetMapping(value = "/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
