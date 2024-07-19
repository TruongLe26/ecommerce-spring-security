package org.example.ss.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.ss.dtos.RegisterUserDto;
import org.example.ss.entities.Role;
import org.example.ss.entities.RoleEnum;
import org.example.ss.entities.User;
import org.example.ss.repositories.RoleRepository;
import org.example.ss.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    // Check if email is valid
    public boolean checkValidEmail(RegisterUserDto input) {
        String emailPattern = ".*@gmail\\.com$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(input.getEmail());

        return matcher.matches();
    }


    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public List<User> allSellers() {
        return userRepository.findByRoleName(RoleEnum.SELLER);
    }

    public User createSeller(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SELLER);
        if (optionalRole.isEmpty()) { return null; }

        var user = User
                .builder()
                .fullName(input.getFullName())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword()))
                .role(optionalRole.get())
                .build();
        return userRepository.save(user);
    }
}
