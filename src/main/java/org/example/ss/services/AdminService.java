package org.example.ss.services;

import lombok.RequiredArgsConstructor;
import org.example.ss.entities.User;
import org.example.ss.models.dtos.LoginUserDto;
import org.example.ss.models.responses.LoginResponse;
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
