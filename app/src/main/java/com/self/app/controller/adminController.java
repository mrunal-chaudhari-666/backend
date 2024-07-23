package com.self.app.controller;

import com.self.app.entity.User;
import com.self.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class adminController {
    @Autowired
    private UserService userService;
    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUser(){
        List<User> all =userService.getAll();
        if(all!=null){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/create-new-admin")
    public ResponseEntity<?> saveAdmin(@RequestBody User user){
        userService.saveAdmin(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
