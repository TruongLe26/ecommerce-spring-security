package org.example.ss.bootstrap;

import lombok.RequiredArgsConstructor;
import org.example.ss.models.dtos.RegisterUserDto;
import org.example.ss.entities.Role;
import org.example.ss.entities.RoleEnum;
import org.example.ss.entities.User;
import org.example.ss.repositories.RoleRepository;
import org.example.ss.repositories.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createAdmin();
    }

    private void createAdmin() {
        RegisterUserDto userDto = RegisterUserDto
                .builder()
                .fullName("Admin")
                .email("admin@gmail.com")
                .password("123456")
                .build();
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (optionalRole.isEmpty() || optionalUser.isPresent()) { return; }

        var user = User
                .builder()
                .fullName(userDto.getFullName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(optionalRole.get())
                .build();
        userRepository.save(user);
    }
}
