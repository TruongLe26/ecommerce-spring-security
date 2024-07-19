package org.example.ss.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.ss.dtos.RegisterUserDto;
import org.example.ss.entities.User;
import org.example.ss.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createSeller(@RequestBody RegisterUserDto registerUserDto) {
        User createSeller = userService.createSeller(registerUserDto);
        return ResponseEntity.ok(createSeller);
    }
}
