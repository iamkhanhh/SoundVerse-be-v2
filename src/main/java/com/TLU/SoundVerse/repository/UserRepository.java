package com.TLU.SoundVerse.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.TLU.SoundVerse.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByVerificationCode(String verificationcode);

    void deleteById(Integer id);


    @Query("SELECT COUNT(u) FROM User u")
    Integer countUsers();

    // Đếm số user mới trong tháng
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :startOfMonth")
    Integer countNewUsersThisMonth(LocalDateTime startOfMonth);

    // Đếm tổng số artist (giả sử role artist là 'ARTIST')
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'ARTIST'")
    Integer countArtists();
}
