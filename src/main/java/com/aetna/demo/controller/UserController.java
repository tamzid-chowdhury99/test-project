package com.aetna.demo.controller;
import com.aetna.demo.model.User;
import com.aetna.demo.model.UserLogin;
import com.aetna.demo.service.UserService;
import com.cvs.digital.hc.common.error.model.ResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @GetMapping("/")
    @Operation(summary = "Get all registered users")
    public ResponseEntity<ResponseModel<List<User>>> getAllUsers() {
            List<User> users = userService.getAllUsers();
            return new ResponseEntity<>(new ResponseModel<>(users),HttpStatus.OK);
    }

    @PostMapping("/login")
    @Operation(summary = "Login and authenticate user based on their credentials")
    public ResponseEntity<String> login(@RequestBody UserLogin userLogin) {
        return new ResponseEntity<>(userService.login(userLogin),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user based on their userId")
    public ResponseEntity<ResponseModel<User>> getUserById(@PathVariable(value = "id") String userId) {
            User user = userService.getUserById(userId);
            return new ResponseEntity<>(new ResponseModel<>(user),HttpStatus.OK);
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<String> register(@RequestBody User user) {
            String userId = userService.addUser(user);
            return new ResponseEntity<>("Added new user with ID: " + userId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a users information")
    public ResponseEntity<String> updateUser(@PathVariable("id") String userId, @RequestBody User user){
            userService.updateUser(userId, user);
            return new ResponseEntity<>("User updated with ID: " + userId, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<String> deleteUser(@PathVariable(value = "id") String userId) {
            userService.deleteUserById(userId);
            return new ResponseEntity<>("User deleted with ID: " + userId, HttpStatus.OK);
    }

}