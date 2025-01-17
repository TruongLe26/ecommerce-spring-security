package com.example.ss.services;

import lombok.RequiredArgsConstructor;
import com.example.ss.models.entities.User;
import com.example.ss.models.dtos.LoginUserDto;
import com.example.ss.models.dtos.RegisterUserDto;
import com.example.ss.models.responses.LoginResponse;
import com.example.ss.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public User authenticateByRole(LoginUserDto loginUserDto, String role) throws AccessDeniedException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getEmail(),
                        loginUserDto.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role))) {
            return userRepository.findByEmail(loginUserDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } else {
            throw new AccessDeniedException("User does not have the required role (" + role + ")");
        }
    }

    public LoginResponse authenticateUserAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException {
        User authenticatedUser = authenticateByRole(loginUserDto, "USER");
        return buildLoginResponse(authenticatedUser);
    }

    public LoginResponse authenticateSellerAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException {
        User authenticatedUser = authenticateByRole(loginUserDto, "SELLER");
        return buildLoginResponse(authenticatedUser);
    }

    public LoginResponse authenticateAdminAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException {
        User authenticatedUser = authenticateByRole(loginUserDto, "ADMIN");
        return buildLoginResponse(authenticatedUser);
    }

    public LoginResponse buildLoginResponse(User authenticatedUser) {
        String jwtToken = jwtService.generateToken(authenticatedUser);
        return LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }

    public boolean checkValidEmail(RegisterUserDto registerUserDto) {
        String emailPattern = ".*@gmail\\.com$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(registerUserDto.getEmail());

        return matcher.matches();
    }

}
