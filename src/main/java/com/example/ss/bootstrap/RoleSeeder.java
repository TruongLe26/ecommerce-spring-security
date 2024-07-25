package com.example.ss.bootstrap;

import com.example.ss.models.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import com.example.ss.models.entities.Role;
import com.example.ss.repositories.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loadRoles();
    }

    private void loadRoles() {
        RoleEnum[] roleNames = new RoleEnum[] {
                RoleEnum.USER,
                RoleEnum.SELLER,
                RoleEnum.ADMIN
        };
        Map<RoleEnum, String> roleDescriptionMap = Map.of(
                RoleEnum.USER, "Default user role",
                RoleEnum.SELLER, "Seller role",
                RoleEnum.ADMIN, "Administrator role"
        );

        Arrays.stream(roleNames).forEach(roleName -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);
            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role roleToCreate = Role
                        .builder()
                        .name(roleName)
                        .description(roleDescriptionMap.get(roleName))
                        .build();
                roleRepository.save(roleToCreate);
            });
        });
    }

}
