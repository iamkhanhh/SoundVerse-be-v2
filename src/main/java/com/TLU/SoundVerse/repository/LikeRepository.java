package com.TLU.SoundVerse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.TLU.SoundVerse.entity.Like;
import com.TLU.SoundVerse.entity.Music;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    
    @Query("SELECT l.musicId, COUNT(l.id) AS likeCount FROM Like l GROUP BY l.musicId ORDER BY likeCount DESC")
    List<Object[]> findTopLikedMusic();

    boolean existsByUserIdAndMusicId(Integer userId, Integer musicId);
    
    void deleteByUserIdAndMusicId(Integer userId, Integer musicId);

    Optional<Like> findByUserIdAndMusicId(Integer userId, Integer musicId);

    @Query("SELECT m FROM Like l JOIN Music m ON l.musicId = m.id WHERE l.userId = :userId")
    List<Music> findFavoriteMusicByUserId(@Param("userId") Integer userId);

}
