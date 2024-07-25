package com.example.ss.security;

public interface TokenBlackList {
    void addToBlacklist(String token);
    boolean isBlacklisted(String token);
}
