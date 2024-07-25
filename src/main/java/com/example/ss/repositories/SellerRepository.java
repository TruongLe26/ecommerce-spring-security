package com.example.ss.repositories;

import com.example.ss.models.entities.Role;
import com.example.ss.models.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface SellerRepository extends CrudRepository<User, Long> {

    @Query(
            "SELECT u FROM User u " +
            "JOIN u.roles r " +
            "WHERE u.email = :email AND r.name = 'SELLER'"
    )
    Optional<User> findUserWithSellerRoleByEmail(@Param("email") String email);

}
