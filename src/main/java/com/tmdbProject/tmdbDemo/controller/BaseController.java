package com.tmdbProject.tmdbDemo.controller;

import com.tmdbProject.tmdbDemo.entity.Token;
import com.tmdbProject.tmdbDemo.entity.User;
import com.tmdbProject.tmdbDemo.exceptions.TokenNotFoundException;
import com.tmdbProject.tmdbDemo.exceptions.UserNotFoundException;
import com.tmdbProject.tmdbDemo.service.TokenService;
import com.tmdbProject.tmdbDemo.service.UserService;

public abstract class BaseController {

    protected static final String PARAM_TOKEN = "token";
    TokenService tokenService;
    UserService userService;

    protected boolean methodAuth(String grantedRole, String token) {

        try {
            Token userToken = tokenService.findByToken(token); // returns UserNotFoundException if no user has found with this id

            // There should be definitely a user assigned to this token
            String userRole = userService.findById(userToken.getId()).getUserRole();

            return userRole.equals(grantedRole);
        }
        catch (UserNotFoundException e) {
            return false;
        }
    }
}

