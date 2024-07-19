package org.example.ss.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ss.dtos.LoginUserDto;
import org.example.ss.dtos.RegisterUserDto;
import org.example.ss.entities.User;
import org.example.ss.responses.LoginResponse;
import org.example.ss.services.AuthenticationService;
import org.example.ss.services.JwtService;
import org.example.ss.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RealUserController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signUp(registerUserDto);
        if (registeredUser == null) {
            Map<String, Object> errorResponseBody = new HashMap<>();
            errorResponseBody.put("message", "User already existed");
            errorResponseBody.put("status", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponseBody);
        }
        Map<String, Object> successResponseBody = new HashMap<>();
        successResponseBody.put("message", "User registered successfully");
        successResponseBody.put("status", HttpStatus.OK.value());
        return ResponseEntity.ok(successResponseBody);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws AccessDeniedException {
        // 403
        User authenticatedUser = authenticationService.authenticateRealUser(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = LoginResponse
                .builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
        return ResponseEntity.ok(loginResponse);
    }

}
