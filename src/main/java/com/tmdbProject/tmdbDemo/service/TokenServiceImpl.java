package com.tmdbProject.tmdbDemo.service;

import com.tmdbProject.tmdbDemo.dao.TokenRepository;
import com.tmdbProject.tmdbDemo.entity.Token;
import com.tmdbProject.tmdbDemo.entity.User;
import com.tmdbProject.tmdbDemo.exceptions.TokenNotFoundException;
import com.tmdbProject.tmdbDemo.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    private TokenRepository tokenRepository;

    @Autowired
    public TokenServiceImpl(TokenRepository theTokenRepository) {
        tokenRepository = theTokenRepository;
    }

    @Override
    public Token findByToken(String token) {

        Token theToken = tokenRepository.findByToken(token);

        if(theToken == null)
            throw new UserNotFoundException("No user has been found with this token");

        return theToken;
    }

    @Override
    public Token findById(int id) {

        Token theToken = tokenRepository.findById(id);
        // optional is an object that may or may not contain a non-null value
        // result of findById may not contain a User

        if(theToken != null)
            return theToken;
        else
            throw new TokenNotFoundException("Token with this id is not found");

    }

    @Override
    public void save(int theId, String theToken) {

        Token token = new Token(theId, theToken);

        tokenRepository.save(token);
    }

    @Override
    public int deleteByToken(String theToken) {
        return tokenRepository.deleteByToken(theToken);
    }
}
