package com.example.ss.repositories;

import com.example.ss.models.entities.ForgotPassword;
import com.example.ss.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
    @Query("SELECT fp FROM ForgotPassword fp WHERE fp.otp = ?1 AND fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM ForgotPassword fp WHERE fp.id = ?1")
    void deleteById(Integer id);
}
