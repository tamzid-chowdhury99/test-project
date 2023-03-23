package com.aetna.demo.controller;

import com.aetna.demo.exception.UserCollectionException;
import com.aetna.demo.model.User;
import com.aetna.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        try{
            List<User> users = userService.getAllUsers();
            return new ResponseEntity<>(users,HttpStatus.OK);
        }
        catch(UserCollectionException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") String userId) {
        try{
            User user = userService.getUserById(userId);
            return new ResponseEntity<>(user,HttpStatus.OK);
        }
        catch(UserCollectionException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            String userId = userService.addUser(user);
            return new ResponseEntity<>("Added new user with ID: " + userId, HttpStatus.OK);
        }
        catch(ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(UserCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") String userId, @RequestBody User user){
        try {
            userService.updateUser(userId, user);
            return new ResponseEntity<>("Customer updated with ID: " + userId, HttpStatus.OK);
        }
        catch(ConstraintViolationException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        catch(UserCollectionException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "id") String userId) {
        try{
            userService.deleteUserById(userId);
            return new ResponseEntity<>("Customer deleted with ID: " + userId, HttpStatus.OK);
        }
        catch(UserCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

}