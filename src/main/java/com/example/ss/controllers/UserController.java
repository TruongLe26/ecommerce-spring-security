package com.example.ss.controllers;

import com.example.ss.common.api.CommonResult;
import com.example.ss.services.OtpService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.example.ss.exceptions.UserAlreadyExistedException;
import com.example.ss.models.dtos.LoginUserDto;
import com.example.ss.models.dtos.RegisterUserDto;
import com.example.ss.models.entities.User;
import com.example.ss.models.responses.LoginResponse;
import com.example.ss.services.AuthenticationService;
import com.example.ss.services.JwtService;
import com.example.ss.services.UserService;
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
    public CommonResult<User> signup(@RequestBody RegisterUserDto registerUserDto) throws UserAlreadyExistedException {
        User user = userService.signUp(registerUserDto);
        return CommonResult.success(user);
    }

    @PostMapping("/login")
    public CommonResult<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto, HttpServletResponse response) throws AccessDeniedException {

        LoginResponse loginResponse = userService.authenticateUserAndGetLoginResponse(loginUserDto);

        // add cookie after login
        Cookie cookie = new Cookie("JWT_TOKEN", loginResponse.getToken());
        cookie.setMaxAge(84600);
        cookie.setPath("/");
        response.addCookie(cookie);
        return CommonResult.success(loginResponse);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public CommonResult<User> authenticatedUser() {
        return CommonResult.success(userService.getAuthenticatedUser());
    }
}