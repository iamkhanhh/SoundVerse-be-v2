package com.TLU.SoundVerse.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.TLU.SoundVerse.entity.Music;

@Repository
public interface MusicRepository extends JpaRepository<Music, Integer> {
    List<Music> findByTitleContainingIgnoreCase(String keyword);
    List<Music> findByArtistId(Integer artistId);
    List<Music> findByAlbumsId(Integer albumsId);
    List<Music> findByPlaylistId(Integer playlistId);

    @Query("SELECT COUNT(m) FROM Music m")
    Integer countMusic();

    @Query("SELECT COUNT(m) FROM Music m WHERE m.createdAt >= :startOfMonth")
    Integer countNewMusicThisMonth(LocalDateTime startOfMonth);
}
