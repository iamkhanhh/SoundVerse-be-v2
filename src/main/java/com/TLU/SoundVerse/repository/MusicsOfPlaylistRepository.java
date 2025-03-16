package com.TLU.SoundVerse.repository;

import com.TLU.SoundVerse.entity.MusicsOfPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicsOfPlaylistRepository extends JpaRepository<MusicsOfPlaylist, Integer> {
    List<MusicsOfPlaylist> findByAlbumsId(Integer albumsId);
    void deleteByAlbumsIdAndMusicId(Integer albumsId, Integer musicId);
    List<MusicsOfPlaylist> findByMusicId(Integer albumsId);
}