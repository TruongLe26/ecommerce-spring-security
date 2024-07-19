package org.example.ss.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ss.models.dtos.LoginUserDto;
import org.example.ss.models.dtos.RegisterUserDto;
import org.example.ss.entities.User;
import org.example.ss.models.responses.LoginResponse;
import org.example.ss.services.JwtService;
import org.example.ss.services.SellerService;
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
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SellerController {

    private final JwtService jwtService;
    private final SellerService sellerService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody RegisterUserDto registerUserDto) {
        User registeredSeller = sellerService.signUpSeller(registerUserDto);
        if (registeredSeller == null) {
            Map<String, Object> errorResponseBody = new HashMap<>();
            errorResponseBody.put("message", "Seller already existed");
            errorResponseBody.put("status", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(errorResponseBody);
        }
        Map<String, Object> successResponseBody = new HashMap<>();
        successResponseBody.put("message", "Seller registered successfully");
        successResponseBody.put("status", HttpStatus.OK.value());
        return ResponseEntity.ok(successResponseBody);
//        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws AccessDeniedException {
        User authenticatedUser = sellerService.authenticateRealSeller(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = LoginResponse
                .builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('SELLER')")
    public ResponseEntity<User> authenticatedSeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }
}
