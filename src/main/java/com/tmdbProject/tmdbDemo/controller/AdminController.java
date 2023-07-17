package com.tmdbProject.tmdbDemo.controller;

import com.tmdbProject.tmdbDemo.entity.Token;
import com.tmdbProject.tmdbDemo.entity.User;
import com.tmdbProject.tmdbDemo.exceptions.TokenNotFoundException;
import com.tmdbProject.tmdbDemo.exceptions.UserNotFoundException;
import com.tmdbProject.tmdbDemo.service.MovieService;
import com.tmdbProject.tmdbDemo.service.TokenService;
import com.tmdbProject.tmdbDemo.service.UserService;
import com.tmdbProject.tmdbDemo.service.WatchlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class AdminController extends BaseController {

    private static final String ROLE_ADMIN = "ADMIN";
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public AdminController(UserService theUserService, TokenService theTokenService) {
        userService = theUserService;
        tokenService = theTokenService;
    }

    // add mapping for GET /user - getting information of all the users

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAll(@RequestParam(PARAM_TOKEN) String token) {
        // Only admin can do this operation
        if(!methodAuth(ROLE_ADMIN, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<User> users = userService.findAll();

        return ResponseEntity.ok(users);
    }

    // add mapping for GET /user/{userId} - getting information of specific user

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUser(@PathVariable int userId, @RequestParam(PARAM_TOKEN) String token) {
        // Only admin can do this operation
        if(!methodAuth(ROLE_ADMIN, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User theUser = userService.findById(userId);

        if (theUser != null)
            return ResponseEntity.ok(theUser);
        else
            return ResponseEntity.notFound().build();
    }

    // add mapping for POST /user - add new user

    @PostMapping("/users")
    public ResponseEntity<User> addUser(@RequestBody User theUser, @RequestParam(PARAM_TOKEN) String token) {
        // Only admin can do this operation
        if(!methodAuth(ROLE_ADMIN, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User dbUser;

        try {
            userService.findByEmail(theUser.getEmail());

            dbUser = userService.update(theUser);
        }
        catch(UserNotFoundException e) {
            dbUser = userService.save(theUser);
        }

        if(dbUser != null)
            return ResponseEntity.ok(dbUser);
        else
            return ResponseEntity.notFound().build();

    }

    // add mapping for PUT /employees - update existing employee

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User theUser, @RequestParam("token") String token) {
        // Only admin can do this operation
        if(!methodAuth(ROLE_ADMIN, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User dbUser = userService.update(theUser);

        if(dbUser != null)
            return ResponseEntity.ok(dbUser);
        else
            return ResponseEntity.notFound().build();
    }

    // add mapping for DELETE /employees/{employeeId} - delete employee

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId, @RequestParam("token") String token) {
        // Only admin can do this operation
        if(!methodAuth(ROLE_ADMIN, token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            User tempEmployee = userService.findById(userId); // throws UserNotFoundException if no user found

            Token deletedUserToken = tokenService.findById(userId); //throws TokenNotFound Exception if no token found for that user

            tokenService.deleteByToken(deletedUserToken.getToken());

        }
        catch (UserNotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
        catch(TokenNotFoundException e)
        {
            logger.error(e.getMessage());
        }

        userService.deleteById(userId); // If token is not found just delete the user

        return ResponseEntity.ok("User Id: " + userId + "has been deleted!");
    }

}
