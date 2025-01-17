package com.example.ss.controllers;

import com.example.ss.common.api.CommonResult;
import lombok.RequiredArgsConstructor;
import com.example.ss.exceptions.SellerAlreadyExistedException;
import com.example.ss.models.dtos.LoginUserDto;
import com.example.ss.models.dtos.RegisterUserDto;
import com.example.ss.models.entities.User;
import com.example.ss.models.responses.LoginResponse;
import com.example.ss.services.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/signup")
    public CommonResult<User> signup(@RequestBody RegisterUserDto registerUserDto) throws SellerAlreadyExistedException {
        return CommonResult.success(sellerService.signUpSeller(registerUserDto));
    }

    @PostMapping("/login")
    public CommonResult<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws AccessDeniedException {
        return CommonResult.success(sellerService.authenticateSellerAndGetLoginResponse(loginUserDto));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('SELLER')")
    public CommonResult<User> authenticatedSeller() {
        return CommonResult.success(sellerService.getAuthenticatedSeller());
    }
}
