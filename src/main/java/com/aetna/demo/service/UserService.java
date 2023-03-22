package com.aetna.demo.service;

import com.aetna.demo.model.User;
import com.aetna.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            //create error exception here
            LOGGER.info("No users in the database");
            return null;
        }
        else{
            return users;
        }

    }

    public User getUserById(String userId){
        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            //create error exception here
            LOGGER.info("User with id " + userId + " is not present in database ");
            return null;
        }
        else{
            return user.get();
        }

    }

    public User addUser(User user){
        Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());
        if(existingUser.isPresent()){
            //create error exception here
            LOGGER.info("User with email " + user.getEmail() + " already exists in the database");
            return null;
        }
        else{
            return userRepository.save(user);

        }
    }

    public boolean updateUser(String userId, User user){
        Optional<User> optionalUser = userRepository.findById(userId);

        if(!optionalUser.isPresent()){
            LOGGER.info("Cannot update a user that does not exist");
            return false;
        }

        else {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setPhoneNumber(user.getPhoneNumber());

            userRepository.save(existingUser);
            return true;
        }
    }

    public boolean deleteUserById(String userId){
        Optional<User> existingUser = userRepository.findById(userId);
        if(!existingUser.isPresent()){
            LOGGER.info("Cannot delete a user that does not exist");
            return false;
        }
        else{
            userRepository.delete(existingUser.get());
            LOGGER.info("User with id " + userId + " was deleted");
            return true;
        }

    }


}
