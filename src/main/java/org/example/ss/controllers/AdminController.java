package org.example.ss.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ss.models.dtos.LoginUserDto;
import org.example.ss.entities.User;
import org.example.ss.models.responses.LoginResponse;
import org.example.ss.services.AdminService;
import org.example.ss.services.AuthenticationService;
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

    private final UserService userService;
    private final AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws AccessDeniedException {
        return ResponseEntity.ok(adminService.authenticateAdminAndGetLoginResponse(loginUserDto));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> authenticatedAdmin() {
        return ResponseEntity.ok(adminService.getAuthenticatedAdmin());
    }

    @GetMapping("/sellers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> allSellers() {
        return ResponseEntity.ok(userService.allSellers());
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> allUsers() {
        return ResponseEntity.ok(userService.allUsers());
    }

}
