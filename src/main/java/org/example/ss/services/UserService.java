package org.example.ss.services;

import lombok.RequiredArgsConstructor;
import org.example.ss.exceptions.UserAlreadyExistedException;
import org.example.ss.models.dtos.LoginUserDto;
import org.example.ss.models.dtos.RegisterUserDto;
import org.example.ss.entities.Role;
import org.example.ss.entities.RoleEnum;
import org.example.ss.entities.User;
import org.example.ss.models.responses.LoginResponse;
import org.example.ss.repositories.RoleRepository;
import org.example.ss.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    public User signUp(RegisterUserDto registerUserDto) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        if (optionalRole.isEmpty()) { return null; }

        Optional<User> optionalUser = userRepository.findByEmail(registerUserDto.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistedException(registerUserDto.getEmail());
        }

        var user = User.builder()
                .fullName(registerUserDto.getFullName())
                .email(registerUserDto.getEmail())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .role(optionalRole.get())
                .build();
        return userRepository.save(user);
    }

    public LoginResponse authenticateUserAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException {
        return authenticationService.authenticateUserAndGetLoginResponse(loginUserDto);
    }

    public User getAuthenticatedUser() {
        return (User) authenticationService.getAuthentication().getPrincipal();
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public List<User> allSellers() {
        return userRepository.findByRoleName(RoleEnum.SELLER);
    }

    public User createSeller(RegisterUserDto registerUserDto) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SELLER);
        if (optionalRole.isEmpty()) { return null; }

        var user = User
                .builder()
                .fullName(registerUserDto.getFullName())
                .email(registerUserDto.getEmail())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .role(optionalRole.get())
                .build();
        return userRepository.save(user);
    }
}
