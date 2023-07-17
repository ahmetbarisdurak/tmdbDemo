package com.tmdbProject.tmdbDemo.service;

import com.tmdbProject.tmdbDemo.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(int theId);

    User save(User theUser);

    User update(User theUser);
    void deleteById(int theId);

    User findByEmail(String theEmail);
}
