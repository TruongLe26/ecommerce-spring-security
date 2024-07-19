package org.example.ss.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.ss.dtos.LoginUserDto;
import org.example.ss.dtos.RegisterUserDto;
import org.example.ss.entities.User;
import org.example.ss.responses.LoginResponse;
import org.example.ss.services.AuthenticationService;
import org.example.ss.services.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signUp(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/seller")
    public ResponseEntity<User> registerSeller(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signUpSeller(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
//        User authenticatedUser = authenticationService.authenticate(loginUserDto);
//        String jwtToken = jwtService.generateToken(authenticatedUser);
//        LoginResponse loginResponse = LoginResponse
//                .builder()
//                .token(jwtToken)
//                .expiresIn(jwtService.getExpirationTime())
//                .build();
//        return ResponseEntity.ok(loginResponse);
//    }
}
