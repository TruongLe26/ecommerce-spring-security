package org.example.ss.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ss.models.dtos.LoginUserDto;
import org.example.ss.entities.User;
import org.example.ss.models.responses.LoginResponse;
import org.example.ss.services.AdminService;
import org.example.ss.services.JwtService;
import org.example.ss.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {

    private final JwtService jwtService;
    private final AdminService adminService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws AccessDeniedException {
        User authenticatedUser = adminService.authenticateRealAdmin(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = LoginResponse
                .builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResponseEntity<User> authenticatedAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/sellers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> allSellers() {
        List<User> sellers = userService.allSellers();
        return ResponseEntity.ok(sellers);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.allUsers();
        return ResponseEntity.ok(users);
    }

}
