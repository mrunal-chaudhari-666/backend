package com.self.app.controller;

import com.self.app.entity.User;
import com.self.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;

    @PostMapping("/create-user")
    public void createUser(@RequestBody User user){
    userService.saveNewUser(user);
}
}
