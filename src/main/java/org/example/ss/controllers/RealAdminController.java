package org.example.ss.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ss.dtos.LoginUserDto;
import org.example.ss.dtos.RegisterUserDto;
import org.example.ss.entities.User;
import org.example.ss.responses.LoginResponse;
import org.example.ss.services.AdminService;
import org.example.ss.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class RealAdminController {

    private final JwtService jwtService;
    private final AdminService adminService;

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

}
