package com.TLU.SoundVerse.repository;

import com.TLU.SoundVerse.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    
    @Query("SELECT l.musicId, COUNT(l.id) AS likeCount FROM Like l GROUP BY l.musicId ORDER BY likeCount DESC")
    List<Object[]> findTopLikedMusic();
}
