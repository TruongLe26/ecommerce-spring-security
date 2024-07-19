package org.example.ss.repositories;

import org.example.ss.entities.RoleEnum;
import org.example.ss.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRoleName(RoleEnum roleName);
}
