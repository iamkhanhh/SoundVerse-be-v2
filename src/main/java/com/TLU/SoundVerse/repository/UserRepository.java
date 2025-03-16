package com.TLU.SoundVerse.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.TLU.SoundVerse.entity.User;
import com.TLU.SoundVerse.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationCode(String verificationcode);

    void deleteById(Integer id);


    @Query("SELECT COUNT(u) FROM User u")
    Integer countUsers();

  
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startOfMonth")
    Integer countNewUsersThisMonth(LocalDateTime startOfMonth);

 
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    Integer countArtists(@Param("role") UserRole role);    
}
