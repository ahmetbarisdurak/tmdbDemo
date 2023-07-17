package com.tmdbProject.tmdbDemo.service;

import com.tmdbProject.tmdbDemo.dao.UserRepository;
import com.tmdbProject.tmdbDemo.entity.User;
import com.tmdbProject.tmdbDemo.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository theUserRepository) {
        userRepository = theUserRepository;
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int theId) {

        Optional<User> result = userRepository.findById(theId);
        // optional is an object that may or may not contain a non-null value
        // result of findById may not contain a User

        User theUser = null;

        if(result.isPresent())
            theUser = result.get();
        else
            throw new UserNotFoundException("User id not found - " + theId);

        return theUser;
    }

    @Override
    public User save(User theUser) {
        return userRepository.save(theUser);
    }

    @Override
    public User update(User theUser) {
        userRepository.updateUserByEmail(theUser.getFirstName(), theUser.getLastName(), theUser.getPassword(), theUser.getEmail(), theUser.getUserRole());

        return theUser;
    }

    @Override
    public void deleteById(int theId) {
        userRepository.deleteById(theId);
    }

    @Override
    public User findByEmail(String theEmail) {

        User user = userRepository.findByEmail(theEmail);

        if (user == null) {
            throw new UserNotFoundException("User not found with this email");
        }
        return user;

    }
}
