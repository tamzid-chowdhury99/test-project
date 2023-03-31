package com.aetna.demo.service;

import com.aetna.demo.exception.UserCollectionException;
import com.aetna.demo.model.User;
import com.aetna.demo.model.UserLogin;
import com.aetna.demo.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class UserServiceTest

{
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @InjectMocks
    private UserService userService;

    List<User> userList;
    User user1;
    User user2;
    User user3;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTest.class);
    @BeforeEach
    public void beforeTest(TestInfo testInfo) {
        LOGGER.info("***********************************************************");
        LOGGER.info("STARTING TEST: {}", testInfo.getDisplayName());
        LOGGER.info("***********************************************************");

        //User 1
        user1 = new User("1","Tamzid", "Chowdhury", "tamzid123@gmail.com", "3475998377","testPassword!");
        Mockito.when(userRepository.findById(user1.getUserId()))
                .thenReturn(Optional.of(user1));

        //User 2
        user2  = new User("2","Joe", "Smith", "joe.smith@gmail.com", "6464359837","JoeSmith1#");
        Mockito.when(userRepository.findById(user2.getUserId()))
                .thenReturn(Optional.of(user2));

        //User 3
        user3 = new User("3","Alex", "Brown", "alex.brown@yahoo.com", "1234567890", "BroAlex23@");
        Mockito.when(userRepository.findById(user3.getUserId()))
                .thenReturn(Optional.of(user3));
    }

    @AfterEach
    public void afterTest(TestInfo testInfo) {
        LOGGER.info("***********************************************************");
        LOGGER.info("FINISHED TEST: {}", testInfo.getDisplayName());
        LOGGER.info("***********************************************************");
    }

    @Test
    public void testGetAllUsers(){

        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        //Positive Case - Get all users
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        List<User> users = userService.getAllUsers();
        Assert.assertEquals(users, userList);
        Assert.assertEquals(users.size(), 3);
    }

    @Test
    public void testGetUserById(){

        //Positive case - When user exists (user1 was mocked in the setup function)
        User user = userService.getUserById("1");
        Assert.assertEquals(user1, user);

        //Negative Case - When user does not exist
        Mockito.when(userRepository.findById("5")).thenReturn(Optional.empty());
        Exception exception = Assert.assertThrows(UserCollectionException.class, () -> userService.getUserById("5"));
        Assert.assertEquals("User with id 5 does not exist", exception.getMessage());

    }

    @Test
    public void testLogin(){

        //Positive case - When user successfully logs in
        UserLogin userLogin = new UserLogin("tamzid123@gmail.com","testPassword!");
        Mockito.when(userRepository.findUserByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        String result = userService.login(userLogin);
        Assert.assertEquals("Login was successful. Welcome Tamzid Chowdhury!",result);

        //Negative Case - When user fails to log in
        userLogin.setPassword("testFailPassword!"); //intentionally wrong password
        result = userService.login(userLogin);
        Assert.assertEquals("Login failed, email or password was incorrect",result);

    }

    @Test
    public void testAddNewUser(){

        //Positive Case - User is successfully added
        User newUser = new User("4","New", "User", "new.user@yahoo.com", "1234567890", "newUser123");
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);
        String actual = userService.addUser(newUser);
        Assert.assertEquals("4", actual);

        //Negative Case - Email is already registered. Cannot add user.
        User newUserWithExistingEmail = new User("5","New", "User", "tamzid123@gmail.com", "1234567890", "newUser123");
        Mockito.when(userRepository.findUserByEmail(newUserWithExistingEmail.getEmail())).thenReturn(Optional.of(newUserWithExistingEmail));
        Exception exception = Assert.assertThrows(UserCollectionException.class, () -> userService.addUser(newUserWithExistingEmail));
        Assert.assertEquals("User with email tamzid123@gmail.com already exists", exception.getMessage());
    }

    @Test
    public void testUpdateNewUser(){

        //Positive Case - User is successfully updated, first name updated from Joe to Joseph
        User newUser = new User("2","Joseph", "Smith", "joe.smith@gmail.com", "6464359837","JoeSmith1#");
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);
        String actual = userService.updateUser(newUser.getUserId(), newUser);
        Assert.assertEquals("User with id 2 was updated", actual);

        //Negative Case - Email is already registered. Cannot add user.
        Mockito.when(userRepository.findById("5")).thenReturn(Optional.empty());
        Exception exception = Assert.assertThrows(UserCollectionException.class, () -> userService.updateUser("5", user1));
        Assert.assertEquals("User with id 5 does not exist", exception.getMessage());
    }

    @Test
    public void testDeleteNewUser(){

        //Positive Case - User is successfully deleted
        String actual = userService.deleteUserById("3");
        Assert.assertEquals("User with id 3 was deleted", actual);

        //Negative Case - When user does not exist
        Mockito.when(userRepository.findById("5")).thenReturn(Optional.empty());
        Exception exception = Assert.assertThrows(UserCollectionException.class, () -> userService.deleteUserById("5"));
        Assert.assertEquals("User with id 5 does not exist", exception.getMessage());
    }




}
