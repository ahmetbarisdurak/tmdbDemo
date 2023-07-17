package com.tmdbProject.tmdbDemo.dao;

import com.tmdbProject.tmdbDemo.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String theEmail);

    @Transactional
    //@Modifying
    @Query("UPDATE User u SET u.firstName = :firstName, u.lastName = :lastName, u.password = :password , u.userRole = :userRole WHERE u.email = :email")
    void updateUserByEmail(@Param("firstName") String firstName, @Param("lastName") String lastName,
                           @Param("password") String password, @Param("email") String email, @Param("userRole") String userRole);

}
