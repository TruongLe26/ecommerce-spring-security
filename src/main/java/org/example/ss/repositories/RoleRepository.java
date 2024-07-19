package org.example.ss.repositories;

import org.example.ss.entities.Role;
import org.example.ss.entities.RoleEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnum name);
}
