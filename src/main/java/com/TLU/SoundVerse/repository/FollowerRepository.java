package com.TLU.SoundVerse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.TLU.SoundVerse.entity.Follower;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Integer> {
    List<Follower> findByUserId(Integer userId);

    @Query("SELECT f.artistId, COUNT(f.id) AS followerCount FROM Follower f GROUP BY f.artistId ORDER BY followerCount DESC")
    List<Object[]> findTopFollowedArtists();

    boolean existsByUserIdAndArtistId(Integer userId, Integer artistId);

    void deleteByUserIdAndArtistId(Integer userId, Integer artistId);

    Optional<Follower> findByUserIdAndArtistId(Integer userId, Integer artistId);

    int countByArtistId(Integer artistId);

    @Query("SELECT COUNT(f) FROM Follower f WHERE f.artistId = :artistId")
    int countFollowersByArtist(@Param("artistId") Integer artistId);
}
