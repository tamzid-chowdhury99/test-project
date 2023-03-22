package com.aetna.demo.controller;

import com.aetna.demo.model.User;
import com.aetna.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        if(users == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(users,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") String userId) {
        User user = userService.getUserById(userId);

        if(user == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<User> register(@RequestBody User user) {
        User newUser = userService.addUser(user);

        if(newUser == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        LOGGER.info("User with id {} was added", user.getUserId());
        return new ResponseEntity<>(newUser,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") String userId, @RequestBody User user){
        if(!userService.updateUser(userId, user)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity("Customer updated with ID: " + userId, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable(value = "id", required = true) String userId) {
        if(!userService.deleteUserById(userId)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity("Customer deleted with ID: " + userId, HttpStatus.OK);
        }
    }

}