package com.example.ss.services;

import lombok.RequiredArgsConstructor;
import com.example.ss.models.entities.User;
import com.example.ss.models.dtos.LoginUserDto;
import com.example.ss.models.responses.LoginResponse;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AuthenticationService authenticationService;

    public LoginResponse authenticateAdminAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException {
        return authenticationService.authenticateAdminAndGetLoginResponse(loginUserDto);
    }

    public User getAuthenticatedAdmin() {
        return (User) authenticationService.getAuthentication().getPrincipal();
    }
}
