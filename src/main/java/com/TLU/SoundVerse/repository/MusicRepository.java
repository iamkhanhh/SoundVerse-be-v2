package com.TLU.SoundVerse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TLU.SoundVerse.dto.response.MusicResponse;
import com.TLU.SoundVerse.entity.Music;

@Repository
public interface MusicRepository extends JpaRepository<Music, Integer> {
    List<Music> findByTitleContainingIgnoreCase(String keyword);
    List<MusicResponse> findByArtistId(Integer artistId);
}
