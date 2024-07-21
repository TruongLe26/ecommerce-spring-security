package org.example.ss.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ss.exceptions.UserAlreadyExistedException;
import org.example.ss.models.dtos.LoginUserDto;
import org.example.ss.models.dtos.RegisterUserDto;
import org.example.ss.entities.User;
import org.example.ss.models.responses.LoginResponse;
import org.example.ss.services.AuthenticationService;
import org.example.ss.services.JwtService;
import org.example.ss.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody RegisterUserDto registerUserDto) throws UserAlreadyExistedException {
        return ResponseEntity.ok(userService.signUp(registerUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws AccessDeniedException {
        return ResponseEntity.ok(userService.authenticateUserAndGetLoginResponse(loginUserDto));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> authenticatedUser() {
        return ResponseEntity.ok(userService.getAuthenticatedUser());
    }
}