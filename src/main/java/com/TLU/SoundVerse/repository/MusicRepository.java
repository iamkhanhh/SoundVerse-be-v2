package com.TLU.SoundVerse.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.TLU.SoundVerse.entity.Music;
import com.TLU.SoundVerse.enums.MusicStatus;

@Repository
public interface MusicRepository extends JpaRepository<Music, Integer> {
    List<Music> findByTitleContainingIgnoreCase(String keyword);

    List<Music> findByArtistId(Integer artistId);

    List<Music> findByAlbumsId(Integer albumsId);

    Optional<Music> findById(Integer id);

    @Query("SELECT COUNT(m) FROM Music m")
    Integer countMusic();

    @Query("SELECT COUNT(m) FROM Music m WHERE m.createdAt >= :startOfMonth")
    Integer countNewMusicThisMonth(LocalDateTime startOfMonth);

    List<Music> findByArtistIdAndStatus(Integer artistId, MusicStatus status);

    List<Music> findByStatus(MusicStatus status);

    @Query("SELECT COUNT(m) FROM Music m WHERE m.artistId = :artistId")
    int countMusicByArtist(@Param("artistId") Integer artistId);
}
