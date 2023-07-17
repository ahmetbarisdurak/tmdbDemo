package com.tmdbProject.tmdbDemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name="user_token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="token_id")
    private int tokenId;

    @Column(name="id")
    private int id;

    @Column(name="token")
    private String token;

    public Token() {

    }
    public Token(int id, String token) {
        this.id = id;
        this.token = token;
    }

    public Token(int tokenId, int id, String token) {
        this.tokenId = tokenId;
        this.id = id;
        this.token = token;
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", token='" + token + '\'' +
                '}';
    }
}
