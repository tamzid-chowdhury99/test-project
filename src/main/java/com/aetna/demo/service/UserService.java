package com.aetna.demo.service;

import com.aetna.demo.exception.UserCollectionException;
import com.aetna.demo.model.User;
import com.aetna.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
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

    public List<User> getAllUsers() throws UserCollectionException{
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            LOGGER.info("No users in the database");
            throw new UserCollectionException(UserCollectionException.EmptyUserList());
        }
        else{
            return users;
        }

    }

    public User getUserById(String userId) throws UserCollectionException {
        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            LOGGER.info("User with id " + userId + " is not present in database ");
            throw new UserCollectionException(UserCollectionException.NotFoundException(userId));
        }
        else{
            return user.get();
        }

    }

    public String addUser(User user) throws UserCollectionException, ConstraintViolationException {
        Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());
        if(existingUser.isPresent()){
            LOGGER.info("User with email " + user.getEmail() + " already exists in the database");
            throw new UserCollectionException(UserCollectionException.UserAlreadyExists(user.getEmail()));
        }
        else{
            User newUser = userRepository.save(user);
            LOGGER.info("User with id " + newUser.getUserId() + " was saved");
            return newUser.getUserId();

        }
    }

    public void updateUser(String userId, User user) throws UserCollectionException, ConstraintViolationException {
        Optional<User> optionalUser = userRepository.findById(userId);

        if(!optionalUser.isPresent()){
            LOGGER.info("Cannot update a user that does not exist");
            throw new UserCollectionException(UserCollectionException.NotFoundException(userId));
        }

        else {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setPhoneNumber(user.getPhoneNumber());

            userRepository.save(existingUser);
            LOGGER.info("User with id " + userId + " was updated");
        }
    }

    public void deleteUserById(String userId) throws UserCollectionException{
        Optional<User> existingUser = userRepository.findById(userId);
        if(!existingUser.isPresent()){
            LOGGER.info("Cannot delete a user that does not exist");
            throw new UserCollectionException(UserCollectionException.NotFoundException(userId));
        }
        else{
            userRepository.delete(existingUser.get());
            LOGGER.info("User with id " + userId + " was deleted");
        }

    }


}
