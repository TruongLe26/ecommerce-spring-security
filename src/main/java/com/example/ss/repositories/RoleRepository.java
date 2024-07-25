package com.example.ss.repositories;

import com.example.ss.models.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.ss.models.entities.Role;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(RoleEnum name);

    @Query("SELECT u.roles FROM User u WHERE u.email = :email")
    Optional<Set<Role>> getRolesByEmail(@Param("email") String email);

}
