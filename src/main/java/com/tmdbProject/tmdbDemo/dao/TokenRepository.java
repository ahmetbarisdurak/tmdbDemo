package com.tmdbProject.tmdbDemo.dao;

import com.tmdbProject.tmdbDemo.entity.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    Token findByToken(String token);

    @Query("SELECT u FROM Token u WHERE u.id = ?1")
    Token findById(int id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token u WHERE u.token = ?1")
    int deleteByToken(String token);

}
