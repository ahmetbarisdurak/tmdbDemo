package com.tmdbProject.tmdbDemo.controller;

import com.tmdbProject.tmdbDemo.entity.Token;
import com.tmdbProject.tmdbDemo.entity.User;
import com.tmdbProject.tmdbDemo.exceptions.TokenNotFoundException;
import com.tmdbProject.tmdbDemo.exceptions.UserNotFoundException;
import com.tmdbProject.tmdbDemo.service.TokenService;
import com.tmdbProject.tmdbDemo.service.UserService;
import com.tmdbProject.tmdbDemo.token.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final int BASIC_AUTH_LENGTH = "Basic ".length();
    private static final int EMAIL_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    private UserService userService;
    private TokenService tokenService;


    @Autowired
    public AuthController(UserService theUserService, TokenService theTokenService) {
        userService = theUserService;
        tokenService = theTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(HttpServletRequest request) {
        // Both admin and user can log in into the system
        // takes the request parameter from the basic auth part in postman
        // Retrieve the user from the database based on the provided username
        String auth = request.getHeader(AUTHORIZATION_HEADER);
        String base64Credentials = auth.substring(BASIC_AUTH_LENGTH);

        String credentials = new String(Base64.getDecoder().decode(base64Credentials));
        String[] splitCredentials = credentials.split(":", 2);

        String email = splitCredentials[EMAIL_INDEX];
        String password = splitCredentials[PASSWORD_INDEX];

        try {
            User user = userService.findByEmail(email); // UserNotFoundException is thrown

            // Check if password is right
            if (user.getPassword().equals(password)) {
                try {
                    tokenService.findById(user.getId()); // if id cannot be found throws TokenNotFoundException
                    return ResponseEntity.ok("User already logged in!");
                }
                catch (TokenNotFoundException e) { // If token cannot be found we can log in
                    logger.error(e.getMessage() + "1 1 1 1 1 ");
                    String token = JwtUtils.generateToken(user.getEmail()); // generates token
                    tokenService.save(user.getId(), token); // adds token to database
                    return ResponseEntity.ok("Login successful");
                }
            }
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        catch (UserNotFoundException e) {
            logger.error("Error occurred: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam("token") String token) {

        if(tokenService.deleteByToken(token) > 0 )
            return ResponseEntity.ok("User has been logged out!");

        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no user logged with this token");
    }
}
