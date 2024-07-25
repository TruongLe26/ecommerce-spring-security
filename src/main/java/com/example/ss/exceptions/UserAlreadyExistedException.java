package com.example.ss.exceptions;

public class UserAlreadyExistedException extends RuntimeException {

    public UserAlreadyExistedException(String email) {
        super("User with email " + email + " already existed.");
    }

}
