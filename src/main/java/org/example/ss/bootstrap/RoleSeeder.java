package org.example.ss.bootstrap;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.ss.entities.Role;
import org.example.ss.entities.RoleEnum;
import org.example.ss.repositories.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;

    @Override
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
