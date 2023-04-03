package com.aetna.demo.service;

import com.aetna.demo.exception.UserCollectionException;
import com.aetna.demo.model.User;
import com.aetna.demo.model.UserLogin;
import com.aetna.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.util.List;
import java.util.Optional;


@Service
@Validated
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    public User getUserById(String userId) {
        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            LOGGER.error("User with id " + userId + " is not present in database ");
            throw new UserCollectionException(UserCollectionException.NotFoundException(userId));
        }
        else{
            LOGGER.info("User found with id " + userId);
            return user.get();
        }

    }

    public String login(UserLogin userLogin){
        Optional<User> existingUser = userRepository.findUserByEmail(userLogin.getEmail());
        if(!existingUser.isPresent()){
            LOGGER.error("Login failed, email or password was incorrect");
            return "Login failed, email or password was incorrect";
        }

        if(!(userLogin.getPassword().equals(existingUser.get().getPassword()))){
            LOGGER.error("Login failed, email or password was incorrect");
            return "Login failed, email or password was incorrect";
        }

        return "Login was successful. Welcome " + existingUser.get().getFirstName() + " " + existingUser.get().getLastName() + "!";
    }

    public String addUser(User user) {
        Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());
        if(existingUser.isPresent()){
            LOGGER.error("User with email " + user.getEmail() + " already exists in the database");
            throw new UserCollectionException(UserCollectionException.UserAlreadyExists(user.getEmail()));
        }
        else{
            User newUser = userRepository.save(user);
            LOGGER.error("User with id " + newUser.getUserId() + " was saved");
            return newUser.getUserId();

        }
    }

    public String updateUser(String userId, User user) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if(!optionalUser.isPresent()){
            LOGGER.error("Cannot update a user that does not exist");
            throw new UserCollectionException(UserCollectionException.NotFoundException(userId));
        }

        else {
            userRepository.save(user);
            LOGGER.error("User with id " + userId + " was updated");
            return "User with id " + userId + " was updated";
        }
    }

    public String deleteUserById(String userId) {
        Optional<User> existingUser = userRepository.findById(userId);
        if(!existingUser.isPresent()){
            LOGGER.info("Cannot delete a user that does not exist");
            throw new UserCollectionException(UserCollectionException.NotFoundException(userId));
        }
        else{
            userRepository.delete(existingUser.get());
            LOGGER.info("User with id " + userId + " was deleted");
            return "User with id " + userId + " was deleted";
        }

    }


}
